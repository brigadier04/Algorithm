# 🤖 BackjoonHub, Gemini AI로 PS 리뷰 자동화

BackjoonHubGitHub를 통해 커밋된 PS 파일에 대해 Gemini AI가 자동으로 코드 리뷰 댓글을 달아주는 GitHub Actions 워크플로우입니다.

## 주요 기능

- `main` 브랜치에 `.java` 파일이 푸시될 때 자동 실행
- Gemini AI가 시간/공간 복잡도, 자바 컨벤션, 성능 최적화 관점에서 리뷰
- 리뷰 결과를 해당 커밋에 댓글로 자동 작성

---

## 파일 구조

```
.github/
├── scripts/
│   └── gemini_java_reviewer.py   # Gemini API 호출 및 GitHub 댓글 작성 스크립트
└── workflows/
    └── gemini_review.yml         # GitHub Actions 워크플로우 정의
```

---

## 트러블슈팅 기록

### ❌ 404 NOT_FOUND — 모델을 찾을 수 없음

```
models/gemini-1.5-flash is not found for API version v1beta
```

**원인:** `google-genai` 최신 SDK가 `v1beta` API를 사용하는데, `gemini-1.5-flash`는 해당 버전에서 제거됨

**해결:** 모델명을 `gemini-2.5-flash`로 변경

---

### ❌ 429 RESOURCE_EXHAUSTED — limit: 0

```
Quota exceeded, limit: 0, model: gemini-2.0-flash
```

**원인:** `gemini-2.0-flash`는 2026년 3월부로 deprecated되어 Free Tier 할당량 자체가 0으로 설정됨. 새 프로젝트/계정으로 API 키를 재발급해도 동일하게 발생

**해결:** 모델명을 현재 Free Tier 지원 모델인 `gemini-2.5-flash`로 변경

```python
response = client.models.generate_content(
    model="gemini-2.5-flash",
    contents=prompt
)
```

---

### ❌ 403 Forbidden — 커밋 댓글 작성 권한 없음

```
Resource not accessible by integration: 403
```

**원인:** 워크플로우의 `contents` 권한이 `read`로 설정되어 있어 커밋에 댓글 작성 불가

**해결:** `gemini_review.yml`에서 권한 수정

```yaml
permissions:
  contents: write  # read → write
  issues: write
  pull-requests: write
```
