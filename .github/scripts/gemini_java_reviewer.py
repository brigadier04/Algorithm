import os
from google import genai
from github import Github, Auth

# 환경 변수 로드
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
REPO_NAME = os.getenv("GITHUB_REPOSITORY")
COMMIT_SHA = os.getenv("GITHUB_SHA")

# Gemini 및 GitHub 클라이언트 초기화
client = genai.Client(api_key=GEMINI_API_KEY)
auth = Auth.Token(GITHUB_TOKEN)
g = Github(auth=auth)

def main():
    try:
        repo = g.get_repo(REPO_NAME)
        commit = repo.get_commit(COMMIT_SHA)
        
        # 추가/수정된 자바 파일만 추출
        java_files = [f for f in commit.files if f.filename.endswith(".java") and f.status != "removed"]
        
        if not java_files:
            print("리뷰할 Java 파일이 없습니다.")
            return

        full_review = "🤖 **Gemini AI 자바 코드 리뷰**\n\n"
        
        for file in java_files:
            prompt = f"""
            당신은 자바 알고리즘 전문가입니다. 다음 코드를 리뷰하고 한글로 답변하세요.
            파일명: {file.filename}
            
            리뷰 항목:
            1. 시간/공간 복잡도 분석
            2. 자바 컨벤션 및 가독성 개선점
            3. 성능 최적화 팁
            
            코드 내용:
            {file.patch}
            """
            
            # 최신 모델 호출 방식
            response = client.models.generate_content(
                model="gemini-2.0-flash",
                contents=prompt
            )
            
            full_review += f"### 📄 파일: {file.filename}\n{response.text}\n\n---\n"

        # 커밋에 댓글 작성
        commit.create_comment(full_review)
        print("✅ 리뷰 댓글 작성이 완료되었습니다.")

    except Exception as e:
        print(f"❌ 에러 발생: {str(e)}")
        # 에러 발생 시 워크플로우를 실패로 처리하기 위해 강제 종료
        exit(1)

if __name__ == "__main__":
    main()
