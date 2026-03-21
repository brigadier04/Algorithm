import os
import google.generativeai as genai
from github import Github

# 환경 변수 설정
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
REPO_NAME = os.getenv("GITHUB_REPOSITORY")
COMMIT_SHA = os.getenv("GITHUB_SHA")

# Gemini 설정
genai.configure(api_key=GEMINI_API_KEY)
model = genai.GenerativeModel('gemini-1.5-flash')

def get_changed_java_files():
    g = Github(GITHUB_TOKEN)
    repo = g.get_repo(REPO_NAME)
    commit = repo.get_commit(COMMIT_SHA)
    
    java_files = []
    for file in commit.files:
        # 자바 파일이면서 삭제되지 않은 파일만 추출
        if file.filename.endswith(".java") and file.status != "removed":
            java_files.append((file.filename, file.patch))
    return repo, commit, java_files

def get_review(filename, code_diff):
    prompt = f"""
    당신은 알고리즘 풀이 전문 Java 개발자입니다. 
    다음은 백준허브를 통해 제출된 '{filename}' 파일의 자바 코드 변경사항(diff)입니다.
    
    이 코드에 대해 다음 항목을 중심으로 짧고 명확하게 한글로 리뷰해 주세요:
    1. 코드의 효율성 및 시간 복잡도 분석
    2. 자바 컨벤션(명명 규칙 등) 준수 여부
    3. 더 나은 성능을 위한 개선 제안 (예: Scanner 대신 BufferedReader 사용 등)
    
    코드 내용:
    {code_diff}
    """
    response = model.generate_content(prompt)
    return response.text

def main():
    repo, commit, files = get_changed_java_files()
    
    if not files:
        print("리뷰할 Java 파일이 없습니다.")
        return

    full_review = "🤖 **Gemini AI 코드 리뷰 결과**\n\n"
    for filename, diff in files:
        review = get_review(filename, diff)
        full_review += f"### 📄 파일: {filename}\n{review}\n\n---\n"

    # 커밋에 댓글 달기
    commit.create_comment(full_review)
    print("리뷰 댓글 작성이 완료되었습니다.")

if __name__ == "__main__":
    main()
