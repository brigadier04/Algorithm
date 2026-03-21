# 🚀 Algorithm Solutions with AI Code Review

백준(Baekjoon) 문제 풀이와 Gemini 1.5 Flash API를 활용한 자동 코드 리뷰 시스템입니다.
백준 허브(BaekjoonHub)를 통해 문제를 올리면, AI가 실시간으로 코드의 효율성과 가독성을 분석합니다.

---

## 🛠 Tech Stack

- **Language:** Java
- **Automation:** GitHub Actions
- **AI Engine:** Google Gemini 1.5 Flash API
- **Tool:** BaekjoonHub (Chrome Extension)

---

## 🤖 AI Code Review System

문제를 풀고 리포지토리에 푸시되면, Gemini AI가 다음 항목을 분석하여 **Commit Comment**로 피드백을 남깁니다.

1. **Complexity Analysis:** 시간 및 공간 복잡도 분석
2. **Refactoring:** 더 효율적인 알고리즘이나 자료구조 제안
3. **Clean Code:** 변수명, 가독성 및 코드 컨벤션 개선안

---

## 📂 Project Structure

```text
.
├── .github
│   ├── workflows      # GitHub Actions 자동화 설정
│   └── scripts        # Gemini API 호출용 파이썬 스크립트
├── 백준                # 백준 허브를 통해 자동 업로드된 문제들
└── README.md
