import os
import subprocess
import requests
import google.generativeai as genai

# 1. Gemini 설정
genai.configure(api_key=os.environ["GEMINI_API_KEY"])
model = genai.GenerativeModel('gemini-1.5-flash')

def get_changed_files():
    try:
        # 최신 원격 기준으로 diff
        subprocess.run(['git', 'fetch', 'origin', 'main'], check=True)

        result = subprocess.check_output(
            ['git', 'diff', '--name-only', 'origin/main', 'HEAD'],
            text=True
        )

        files = result.splitlines()

        source_extensions = ('.cpp', '.py', '.java', '.c', '.js', '.ts')
        changed_files = [f for f in files if f.endswith(source_extensions) and '.github' not in f]

        print(f"감지된 변경 파일: {changed_files}")
        return changed_files

    except Exception as e:
        print(f"파일 목록을 가져오는 중 오류 발생: {e}")
        return []

def main():
    changed_files = get_changed_files()
    
    if not changed_files:
        print("리뷰할 변경된 파일이 없습니다. (조건: 최근 커밋에 소스 코드 포함)")
        return

    for file_path in changed_files:
        if not os.path.exists(file_path): 
            print(f"파일을 찾을 수 없음: {file_path}")
            continue
        
        print(f"{file_path} 분석 중...")
        with open(file_path, 'r', encoding='utf-8') as f:
            code = f.read()

        prompt = f"""
        당신은 알고리즘 및 클린 코드 전문가입니다. 다음 소스 코드를 분석하세요.
        파일명: {file_path}

        요구사항:
        1. 시간 및 공간 복잡도를 분석하세요.
        2. 더 효율적인 알고리즘이나 자료구조 제안이 있다면 구체적으로 설명하세요.
        3. 가독성과 변수명 등 클린 코드 관점의 개선안을 주되, 칭찬도 곁들여주세요.
        4. 답변은 한국어로 친절하게 작성하세요.

        코드:
        {code}
        """

        try:
            response = model.generate_content(prompt)
            review_result = response.text

            # 리뷰 파일 임시 저장
            with open("review_msg.txt", "w", encoding='utf-8') as f:
                f.write(f"### 🤖 Gemini AI 알고리즘 리뷰\n\n{review_result}")
            
            # GitHub CLI를 사용하여 댓글 작성
            commit_hash = subprocess.check_output(["git", "rev-parse", "HEAD"], text=True).strip()

            url = f"https://api.github.com/repos/{os.environ['GITHUB_REPOSITORY']}/commits/{commit_hash}/comments"

            headers = {
                "Authorization": f"Bearer {os.environ['GITHUB_TOKEN']}",
                "Accept": "application/vnd.github+json"
            }

            data = {
                "body": f"### 🤖 Gemini AI 알고리즘 리뷰\n\n{review_result}"
            }

            response = requests.post(url, headers=headers, json=data)

            print("댓글 생성 상태:", response.status_code)
            print(response.text)
            
        except Exception as e:
            print(f"Gemini API 호출 또는 댓글 작성 중 오류: {e}")

if __name__ == "__main__":
    main()
