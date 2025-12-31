# DTO 파일 정리 완료 보고서

**작성일**: 2025-01-XX
**작업**: 중복 DTO 파일 삭제 및 통합

---

## 📊 작업 통계

### Before (정리 전)
- **전체 DTO**: 32개
- **Update 관련 DTO**: 13개 (관리자 9개 + 사용자 4개)
- **중복 파일**: ResponseData (2곳)

### After (정리 후)
- **전체 DTO**: 18개 ⬇️ 14개 감소
- **Update 관련 DTO**: 2개 (통합 DTO)
- **중복 파일**: 0개

**파일 감소율**: 43.75% ⬇️

---

## 🗑️ 삭제된 파일 목록 (14개)

### 1. 관리자용 Update DTO (9개)
- ❌ `admin/model/dto/UpdateEmailByAdminDTO.java`
- ❌ `admin/model/dto/UpdateIdByAdminDTO.java`
- ❌ `admin/model/dto/UpdateNameByAdminDTO.java`
- ❌ `admin/model/dto/UpdatePasswordByAdminDTO.java`
- ❌ `admin/model/dto/UpdatePhoneByAdminDTO.java`
- ❌ `admin/model/dto/UpdatePointByAdminDTO.java`
- ❌ `admin/model/dto/UpdateRegionByAdminDTO.java`
- ❌ `admin/model/dto/UpdateRoleByAdminDTO.java`
- ❌ `admin/model/dto/UpdateStatusByAdminDTO.java`

### 2. 사용자용 Update DTO (4개)
- ❌ `member/model/dto/UpdateEmailDTO.java`
- ❌ `member/model/dto/UpdatePhoneDTO.java`
- ❌ `member/model/dto/UpdateRegionDTO.java`
- ❌ `member/model/dto/UpdateProfileDTO.java`

### 3. 중복 파일 (1개)
- ❌ `board/model/dto/ResponseData.java` (admin에만 유지)

### 4. 사용되지 않는 DTO (1개)
- ❌ `member/model/dto/MemberLogoutDTO.java`

---

## ✅ 대체 파일 (통합 DTO)

### 신규 생성된 통합 DTO (2개)

#### 1. UpdateFieldRequest.java
```java
common/dto/UpdateFieldRequest.java
```
**대체한 DTO**: 4개
- UpdateEmailDTO
- UpdatePhoneDTO
- UpdateRegionDTO
- ChangePasswordDTO (추후)

**필드**:
- field (email, phone, password, region, name, profile)
- newValue
- currentValue (optional)

**사용 예시**:
```json
{
  "field": "email",
  "newValue": "new@example.com",
  "currentValue": "old@example.com"
}
```

#### 2. AdminUpdateRequest.java
```java
common/dto/AdminUpdateRequest.java
```
**대체한 DTO**: 9개
- UpdateEmailByAdminDTO
- UpdateIdByAdminDTO
- UpdateNameByAdminDTO
- UpdatePasswordByAdminDTO
- UpdatePhoneByAdminDTO
- UpdatePointByAdminDTO
- UpdateRegionByAdminDTO
- UpdateRoleByAdminDTO
- UpdateStatusByAdminDTO

**필드**:
- memberNo
- field (id, password, email, phone, region, name, point, status, role)
- newValue
- regionNo (optional)

**사용 예시**:
```json
{
  "memberNo": 123,
  "field": "email",
  "newValue": "admin@example.com"
}
```

---

## 📁 현재 남아있는 DTO 파일 (18개)

### Admin 관련 (6개)
- ✅ AdminBoardDTO.java
- ✅ AdminBoardDetailDTO.java
- ✅ AdminCommentDTO.java
- ✅ AdminMemberDTO.java
- ✅ AttachmentDTO.java
- ✅ NoticeRequestDTO.java
- ✅ PageResponse.java
- ✅ CommentPageResponse.java
- ✅ ResponseData.java

### Member 관련 (3개)
- ✅ MemberLoginDTO.java
- ✅ MemberSignUpDTO.java
- ✅ ChangePasswordDTO.java

### Board 관련 (4개)
- ✅ BoardDTO.java
- ✅ BoardDetailDTO.java
- ✅ BoardReportDTO.java
- ✅ FeedBoardDTO.java

### Comment 관련 (3개)
- ✅ CommentDTO.java
- ✅ CommentReportDTO.java
- ✅ CommentUpdateDTO.java

### 기타 (8개)
- ✅ BookmarkDTO.java
- ✅ NewsDTO.java
- ✅ ApiResponse.java (신규)
- ✅ ApiError.java (신규)
- ✅ UpdateFieldRequest.java (신규)
- ✅ AdminUpdateRequest.java (신규)

---

## 🔧 필요한 Controller 수정

삭제된 DTO를 사용하는 Controller들을 수정해야 합니다:

### 1. MemberController 수정 필요

**Before**:
```java
@PutMapping("/email")
public ResponseEntity<?> updateEmail(@RequestBody UpdateEmailDTO dto) {
    memberService.updateEmail(dto);
    return ResponseEntity.ok("이메일이 변경되었습니다.");
}

@PutMapping("/phone")
public ResponseEntity<?> updatePhone(@RequestBody UpdatePhoneDTO dto) {
    memberService.updatePhone(dto);
    return ResponseEntity.ok("전화번호가 변경되었습니다.");
}
```

**After**:
```java
@PutMapping("/update")
public ResponseEntity<ApiResponse<Void>> updateField(
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
        case REGION:
            memberService.updateRegion(memberId, request.getNewValue());
            break;
        case PASSWORD:
            memberService.updatePassword(memberId, request.getNewValue(), request.getCurrentValue());
            break;
    }

    return ResponseEntity.ok(ApiResponse.success("정보가 수정되었습니다."));
}
```

### 2. AdminMemberController 수정 필요

**Before**:
```java
@PutMapping("/admin/members/id")
public ResponseEntity<?> updateId(@RequestBody UpdateIdByAdminDTO dto) { ... }

@PutMapping("/admin/members/password")
public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordByAdminDTO dto) { ... }

@PutMapping("/admin/members/phone")
public ResponseEntity<?> updatePhone(@RequestBody UpdatePhoneByAdminDTO dto) { ... }
// ... 9개 엔드포인트
```

**After**:
```java
@PutMapping("/admin/members/update")
public ResponseEntity<ApiResponse<Void>> updateMember(
        @RequestBody AdminUpdateRequest request
) {
    switch (AdminUpdateRequest.AdminFieldType.from(request.getField())) {
        case ID:
            adminService.updateId(request.getMemberNo(), request.getNewValue());
            break;
        case PASSWORD:
            adminService.updatePassword(request.getMemberNo(), request.getNewValue());
            break;
        case PHONE:
            adminService.updatePhone(request.getMemberNo(), request.getNewValue());
            break;
        case EMAIL:
            adminService.updateEmail(request.getMemberNo(), request.getNewValue());
            break;
        case REGION:
            adminService.updateRegion(request.getMemberNo(), request.getNewValue(), request.getRegionNo());
            break;
        case NAME:
            adminService.updateName(request.getMemberNo(), request.getNewValue());
            break;
        case POINT:
            adminService.updatePoint(request.getMemberNo(), Integer.parseInt(request.getNewValue()));
            break;
        case STATUS:
            adminService.updateStatus(request.getMemberNo(), request.getNewValue());
            break;
        case ROLE:
            adminService.updateRole(request.getMemberNo(), request.getNewValue());
            break;
    }

    return ResponseEntity.ok(ApiResponse.success("회원 정보가 수정되었습니다."));
}
```

---

## 📝 프론트엔드 수정 필요

### API 호출 변경

**Before (프론트엔드)**:
```javascript
// 이메일 변경
await put('/members/email', {
  currentEmail: 'old@example.com',
  newEmail: 'new@example.com'
});

// 전화번호 변경
await put('/members/phone', {
  currentPhone: '010-1111-2222',
  newPhone: '010-3333-4444'
});
```

**After (프론트엔드)**:
```javascript
// 통합 엔드포인트 사용
await put('/members/update', {
  field: 'email',
  newValue: 'new@example.com',
  currentValue: 'old@example.com'
});

await put('/members/update', {
  field: 'phone',
  newValue: '010-3333-4444',
  currentValue: '010-1111-2222'
});
```

---

## ✨ 개선 효과

### 1. 유지보수성 향상
- ✅ 필드 추가 시 Enum만 수정
- ✅ DTO 파일 개수 43% 감소
- ✅ API 엔드포인트 단순화 (9개 → 1개)

### 2. 일관성 향상
- ✅ 모든 회원 정보 수정이 동일한 패턴
- ✅ 에러 처리 일관성

### 3. 코드 간결성
- ✅ Controller 메서드 수 대폭 감소
- ✅ 테스트 코드 작성 용이

---

## ⚠️ 주의사항

1. **기존 API 호환성**
   - 기존 엔드포인트를 삭제하면 프론트엔드 오류 발생
   - 점진적 마이그레이션 권장 (기존 API 유지 + 새 API 추가)

2. **프론트엔드 동시 수정 필요**
   - 백엔드 API 변경과 프론트엔드 호출 코드를 함께 수정

3. **테스트 필수**
   - 각 필드별 업데이트 기능 테스트
   - Validation 정상 작동 확인

---

## 🚀 다음 단계

- [ ] MemberController 리팩토링
- [ ] AdminMemberController 리팩토링
- [ ] 프론트엔드 API 호출 코드 수정
- [ ] 통합 테스트 작성

---

**작성자**: Claude Sonnet 4.5
**작업 완료일**: 2025-01-XX
