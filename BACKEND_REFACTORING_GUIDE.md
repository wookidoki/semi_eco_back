# 백엔드 리팩토링 가이드

**프로젝트**: ECO Spring Boot Backend
**작성일**: 2025-01-XX
**목적**: 프론트엔드 연동 최적화 및 코드 품질 개선

---

## 📊 완료된 리팩토링 작업

### 1. ✅ 공통 응답 포맷 표준화

**생성된 파일**:
- `common/dto/ApiResponse.java` - 성공 응답 포맷
- `common/dto/ApiError.java` - 에러 응답 포맷
- `common/constant/ErrorCode.java` - 에러 코드 정의

#### Before (기존):
```java
@PostMapping("/boards")
public ResponseEntity<?> createBoard(@RequestBody BoardDTO board) {
    boardService.insert(board);
    return ResponseEntity.ok().build();
}
```

#### After (개선):
```java
@PostMapping("/boards")
public ResponseEntity<ApiResponse<BoardDTO>> createBoard(@RequestBody BoardDTO board) {
    BoardDTO result = boardService.insert(board);
    return ResponseEntity.ok(ApiResponse.success("게시글이 작성되었습니다.", result));
}
```

#### 프론트엔드 응답 형식:
```json
{
  "success": true,
  "message": "게시글이 작성되었습니다.",
  "data": { ... },
  "timestamp": "2025-01-XX..."
}
```

---

### 2. ✅ 예외 처리 개선

**개선된 파일**:
- `exception/GlobalExceptionHandler.java` - 전역 예외 핸들러

#### 개선 사항:
- ✅ 일관된 에러 응답 포맷
- ✅ 에러 코드 부여 (AUTH_001, MEMBER_002 등)
- ✅ HTTP 상태 코드 정확성 향상
- ✅ 로깅 추가 (Slf4j)
- ✅ Validation 에러 필드별 표시

#### 에러 응답 예시:
```json
{
  "status": 401,
  "code": "AUTH_001",
  "message": "인증에 실패했습니다.",
  "path": "/api/boards",
  "timestamp": "2025-01-XX...",
  "details": {
    "email": "유효한 이메일 형식이 아닙니다"
  }
}
```

---

### 3. ✅ DTO 중복 제거 (17개 → 2개)

**Before (기존 - 17개 DTO)**:
```
member/model/dto/
├── UpdateEmailDTO
├── UpdatePhoneDTO
├── UpdateProfileDTO
├── UpdateRegionDTO
└── ChangePasswordDTO

admin/model/dto/
├── UpdateEmailByAdminDTO
├── UpdateIdByAdminDTO
├── UpdatePasswordByAdminDTO
├── UpdatePhoneByAdminDTO
├── UpdateRegionByAdminDTO
├── UpdateNameByAdminDTO
├── UpdatePointByAdminDTO
├── UpdateStatusByAdminDTO
└── UpdateRoleByAdminDTO
```

**After (개선 - 2개 통합 DTO)**:
```
common/dto/
├── UpdateFieldRequest      # 일반 사용자용 (5개 DTO 통합)
└── AdminUpdateRequest      # 관리자용 (9개 DTO 통합)
```

#### 사용 예시:

**일반 사용자 - 이메일 변경**:
```java
@PutMapping("/members/update")
public ResponseEntity<ApiResponse<Void>> updateMemberField(
        @RequestBody UpdateFieldRequest request,
        Authentication auth
) {
    String memberId = auth.getName();

    switch (UpdateFieldRequest.FieldType.from(request.getField())) {
        case EMAIL:
            memberService.updateEmail(memberId, request.getNewValue(), request.getCurrentValue());
            break;
        case PHONE:
            memberService.updatePhone(memberId, request.getNewValue(), request.getCurrentValue());
            break;
        // ...
    }

    return ResponseEntity.ok(ApiResponse.success("정보가 수정되었습니다."));
}
```

**프론트엔드 요청**:
```javascript
// 이메일 변경
await put('/members/update', {
  field: 'email',
  newValue: 'new@example.com',
  currentValue: 'old@example.com'
});

// 전화번호 변경
await put('/members/update', {
  field: 'phone',
  newValue: '010-1234-5678',
  currentValue: '010-9876-5432'
});
```

**관리자 - 회원 정보 수정**:
```java
@PutMapping("/admin/members/update")
public ResponseEntity<ApiResponse<Void>> updateMemberByAdmin(
        @RequestBody AdminUpdateRequest request
) {
    switch (AdminUpdateRequest.AdminFieldType.from(request.getField())) {
        case EMAIL:
            adminMemberService.updateEmail(request.getMemberNo(), request.getNewValue());
            break;
        case POINT:
            adminMemberService.updatePoint(request.getMemberNo(), Integer.parseInt(request.getNewValue()));
            break;
        // ...
    }

    return ResponseEntity.ok(ApiResponse.success("회원 정보가 수정되었습니다."));
}
```

---

## 🚀 Controller 리팩토링 가이드

### 패턴 1: 성공 응답 반환

**Before**:
```java
@GetMapping("/boards/{id}")
public ResponseEntity<?> getBoard(@PathVariable int id) {
    BoardDTO board = boardService.findById(id);
    return ResponseEntity.ok(board);
}
```

**After**:
```java
@GetMapping("/boards/{id}")
public ResponseEntity<ApiResponse<BoardDTO>> getBoard(@PathVariable int id) {
    BoardDTO board = boardService.findById(id);
    return ResponseEntity.ok(ApiResponse.success(board));
}
```

---

### 패턴 2: 메시지 포함 응답

**Before**:
```java
@PostMapping("/boards")
public ResponseEntity<?> createBoard(@RequestBody BoardDTO board) {
    boardService.insert(board);
    return ResponseEntity.ok("게시글이 작성되었습니다.");
}
```

**After**:
```java
@PostMapping("/boards")
public ResponseEntity<ApiResponse<BoardDTO>> createBoard(@RequestBody BoardDTO board) {
    BoardDTO result = boardService.insert(board);
    return ResponseEntity.ok(ApiResponse.success("게시글이 작성되었습니다.", result));
}
```

---

### 패턴 3: 에러 발생 시

**Before**:
```java
@GetMapping("/boards/{id}")
public ResponseEntity<?> getBoard(@PathVariable int id) {
    BoardDTO board = boardService.findById(id);
    if (board == null) {
        return ResponseEntity.badRequest().body("게시글을 찾을 수 없습니다.");
    }
    return ResponseEntity.ok(board);
}
```

**After**:
```java
@GetMapping("/boards/{id}")
public ResponseEntity<ApiResponse<BoardDTO>> getBoard(@PathVariable int id) {
    BoardDTO board = boardService.findById(id);
    if (board == null) {
        throw new FindFailureException("게시글을 찾을 수 없습니다.");
        // GlobalExceptionHandler가 자동으로 ApiError 반환
    }
    return ResponseEntity.ok(ApiResponse.success(board));
}
```

---

### 패턴 4: Validation 에러 처리

**Before**:
```java
@PostMapping("/members")
public ResponseEntity<?> signUp(@RequestBody @Valid MemberSignUpDTO dto, BindingResult result) {
    if (result.hasErrors()) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
    memberService.signUp(dto);
    return ResponseEntity.ok("회원가입 완료");
}
```

**After**:
```java
@PostMapping("/members")
public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid MemberSignUpDTO dto) {
    // Validation 에러는 GlobalExceptionHandler가 자동 처리
    memberService.signUp(dto);
    return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
}
```

---

## 🔒 보안 설정 개선 (다음 단계)

### application.yml에서 환경변수로 분리

**Before (위험)**:
```yaml
jwt:
  secret: H7oIaBujRo6fA/a0wdk09iY6STECQZbemMj8bcs5xBMzd0IYxzT+hNQd+fgXvsyz3qHF3DIwuYXUE9m7w5tkDw==

naver:
  client:
    id: jZ8qrYWQZsYsih0mCXjZ
    secret: EtAAgiCzjz

spring:
  datasource:
    password: dlwlsltks
```

**After (안전)**:
```yaml
jwt:
  secret: ${JWT_SECRET}

naver:
  client:
    id: ${NAVER_CLIENT_ID}
    secret: ${NAVER_CLIENT_SECRET}

spring:
  datasource:
    password: ${DB_PASSWORD}
```

**환경변수 설정** (IntelliJ):
```
Run -> Edit Configurations -> Environment Variables:
JWT_SECRET=your_jwt_secret
NAVER_CLIENT_ID=your_client_id
NAVER_CLIENT_SECRET=your_client_secret
DB_PASSWORD=your_db_password
```

---

## 📁 파일 업로드 통일 (다음 단계)

### 현재 문제점:
- S3와 로컬 저장 방식 혼재
- 프로필 이미지는 로컬, 게시글 첨부는 S3

### 개선 방향:
```java
public interface FileUploadStrategy {
    String upload(MultipartFile file, String directory);
    void delete(String fileKey);
}

@Service
public class S3FileUploadStrategy implements FileUploadStrategy {
    // S3 업로드 구현
}

@Service
public class LocalFileUploadStrategy implements FileUploadStrategy {
    // 로컬 저장 구현
}

@Configuration
public class FileUploadConfig {
    @Bean
    public FileUploadStrategy fileUploadStrategy(@Value("${file.upload.type}") String type) {
        return "s3".equals(type) ? new S3FileUploadStrategy() : new LocalFileUploadStrategy();
    }
}
```

---

## 📝 로깅 개선 (다음 단계)

### System.out.println() 제거

**Before**:
```java
System.out.println("프로필 이미지 변경: " + memberId);
```

**After**:
```java
@Slf4j
public class MemberController {
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(...) {
        log.info("프로필 이미지 변경 요청: memberId={}", memberId);
        // ...
        log.debug("프로필 이미지 업로드 완료: {}", imageUrl);
    }
}
```

---

## ✅ 리팩토링 체크리스트

각 Controller를 리팩토링할 때 다음 항목을 확인하세요:

- [ ] 모든 응답을 `ApiResponse<T>` 또는 `ApiError`로 반환
- [ ] 에러 발생 시 적절한 Exception throw (GlobalExceptionHandler가 처리)
- [ ] HTTP 상태 코드 정확성 확인 (200, 201, 400, 401, 404, 500)
- [ ] Update 관련 DTO를 `UpdateFieldRequest` 또는 `AdminUpdateRequest`로 대체
- [ ] Validation 에러는 GlobalExceptionHandler에 위임
- [ ] `System.out.println()` → `log.info/debug/warn/error()`
- [ ] 민감한 정보 로깅 방지 (비밀번호, 토큰 등)

---

## 🎯 다음 단계 우선순위

1. **높음** (배포 전 필수)
   - [ ] 보안 설정을 환경변수로 분리
   - [ ] 모든 Controller를 ApiResponse 패턴으로 변경
   - [ ] Update DTO 17개를 통합 DTO로 교체

2. **중간** (기능 개선)
   - [ ] 파일 업로드 전략 통일
   - [ ] 로깅 개선 (Slf4j)
   - [ ] JWT 토큰 만료 시간 조정 (24h → 1h)

3. **낮음** (선택사항)
   - [ ] API 문서화 (Swagger)
   - [ ] 단위 테스트 작성
   - [ ] 성능 최적화 (N+1 쿼리 등)

---

## 💡 프론트엔드 연동 가이드

### API 호출 예시 (프론트엔드)

**성공 응답 처리**:
```typescript
import { showSuccess, showError } from '../utils/toast';
import { post } from '../utils/api';

try {
  const response = await post('/boards', boardData);

  if (response.data.success) {
    showSuccess(response.data.message);
    // response.data.data 사용
  }
} catch (error) {
  // error.response.data가 ApiError 형식
  if (error.response.data.code === 'AUTH_001') {
    // 인증 에러 처리
  }
  showApiError(error);
}
```

**에러 코드별 처리**:
```javascript
const ERROR_HANDLERS = {
  'AUTH_001': () => router.push('/login'),
  'AUTH_003': () => refreshToken(),
  'MEMBER_006': () => showWarning('정지된 계정입니다.'),
};

if (ERROR_HANDLERS[error.response.data.code]) {
  ERROR_HANDLERS[error.response.data.code]();
}
```

---

## 📚 참고 자료

- [Spring Boot Exception Handling Best Practices](https://www.baeldung.com/exception-handling-for-rest-with-spring)
- [RESTful API Design Guide](https://restfulapi.net/)
- [HTTP Status Codes](https://httpstatuses.com/)

---

**작성**: 2025-01-XX
**업데이트**: 리팩토링 진행 시 지속 업데이트
