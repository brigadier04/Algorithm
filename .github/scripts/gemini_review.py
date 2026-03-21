import os
import subprocess
import google.generativeai as genai

# 1. 환경 변수 및 Gemini 설정
genai.configure(api_key=os.environ["GEMINI_API_KEY"])
model = genai.GenerativeModel('gemini-1.5-flash')

def get_changed_files():
    """최근 커밋에서 변경된 파일 목록을 가져옵니다."""
    try:
        # 최근 1개 커밋에서 추가/수정된 파일 확인
        result = subprocess.check_output(['git', 'diff', '--name-only', 'HEAD^', 'HEAD'], text=True)
        files = result.splitlines()
        # 알고리즘 소스 파일만 필터링 (cpp, py, java 등)
        return [f for f in files if f.endswith(('.cpp', '.py', '.java', '.c', '.js'))]
    except:
        return []

def main():
    changed_files = get_changed_files()
    if not changed_files:
        print("리뷰할 변경된 파일이 없습니다.")
        return

    for file_path in changed_files:
        if not os.path.exists(file_path): continue
        
        with open(file_path, 'r', encoding='utf-8') as f:
            code = f.read()

        # 2. 페르소나를 부여한 프롬프트 구성
        prompt = f"""
        당신은 알고리즘 및 클린 코드 전문가입니다. 다음 제출된 소스 코드를 분석하세요.
        파일명: {file_path}

        요구사항:
        1. 시간 및 공간 복잡도를 분석하세요.
        2. 더 효율적인 방식(자료구조, 알고리즘 개선)이 있다면 구체적으로 제안하세요.
        3. 변수명이나 가독성 측면에서 개선할 점을 제안하세요.
        4. 답변은 한국어로 친절하게 작성하세요.

        소스 코드:
        {code}
        """

        # 3. Gemini API 호출
        response = model.generate_content(prompt)
        review_result = response.text

        # 4. 리뷰 내용을 커밋 댓글로 등록 (GitHub CLI 활용)
        with open("review_msg.txt", "w", encoding='utf-8') as f:
            f.write(f"### 🤖 Gemini AI 알고리즘 리뷰\n\n{review_result}")
        
        # GitHub CLI를 사용하여 현재 커밋에 댓글 작성
        commit_hash = os.popen("git rev-parse HEAD").read().strip()
        os.system(f"gh api repos/$GITHUB_REPOSITORY/commits/{commit_hash}/comments -F body=@review_msg.txt")

if __name__ == "__main__":
    main()
