# CoreON
인트라넷 구축 프로넥트 CoreON 26.1.21 ~

Member테이블
```
CREATE TABLE `member` (
	`employee_no` INT NOT NULL AUTO_INCREMENT,
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`pw` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_general_ci',
	`username` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`email` VARCHAR(120) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`mobile` VARCHAR(20) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`dept` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`position` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`role` ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER' COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`employee_no`) USING BTREE,
	UNIQUE INDEX `uk_member_id` (`id`) USING BTREE,
	UNIQUE INDEX `uk_member_email` (`email`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=10003
;
```

Notice 테이블
```
CREATE TABLE `notice` (
	`notice_id` BIGINT NOT NULL AUTO_INCREMENT,
	`category` VARCHAR(30) NOT NULL COLLATE 'utf8mb4_general_ci',
	`title` VARCHAR(200) NOT NULL COLLATE 'utf8mb4_general_ci',
	`content` LONGTEXT NOT NULL COLLATE 'utf8mb4_general_ci',
	`publisher_dept` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`created_by` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
	`created_at` DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	`updated_at` DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP) ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`notice_id`) USING BTREE,
	INDEX `idx_notice_latest` (`created_at`) USING BTREE,
	INDEX `idx_notice_category_latest` (`category`, `created_at`) USING BTREE,
	INDEX `idx_notice_created_by` (`created_by`, `created_at`) USING BTREE,
	CONSTRAINT `fk_notice_created_by` FOREIGN KEY (`created_by`) REFERENCES `member` (`id`) ON UPDATE CASCADE ON DELETE RESTRICT
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;

```

Notice_attachment 테이블
```
CREATE TABLE `notice_attachment` (
	`attachment_id` BIGINT NOT NULL AUTO_INCREMENT,
	`notice_id` BIGINT NOT NULL,
	`originalname` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_general_ci',
	`storedurl` VARCHAR(1024) NOT NULL COLLATE 'utf8mb4_general_ci',
	`content_type` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`sizebytes` BIGINT NULL DEFAULT NULL,
	`created_at` DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	PRIMARY KEY (`attachment_id`) USING BTREE,
	INDEX `idx_notice_attachment` (`notice_id`) USING BTREE,
	CONSTRAINT `fk_notice_attachment_notice` FOREIGN KEY (`notice_id`) REFERENCES `notice` (`notice_id`) ON UPDATE NO ACTION ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
```

board_attachment 테이블
```
CREATE TABLE `board_attachment` (
	`attachment_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`board_id` BIGINT(20) NOT NULL,
	`original_name` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_general_ci',
	`stored_url` VARCHAR(500) NOT NULL COLLATE 'utf8mb4_general_ci',
	`content_type` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`size_bytes` BIGINT(20) NULL DEFAULT NULL,
	`created_at` DATETIME NOT NULL DEFAULT current_timestamp(),
	PRIMARY KEY (`attachment_id`) USING BTREE,
	INDEX `idx_board_id` (`board_id`) USING BTREE,
	CONSTRAINT `fk_attachment_board` FOREIGN KEY (`board_id`) REFERENCES `board_post` (`board_id`) ON UPDATE RESTRICT ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=6
;

```
board_post 테이블
```

CREATE TABLE `board_post` (
	`board_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`category` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`title` VARCHAR(200) NOT NULL COLLATE 'utf8mb4_general_ci',
	`content` TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`dept` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`author_id` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`author_name` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
	`view_count` BIGINT(20) NOT NULL DEFAULT '0',
	`created_at` DATETIME NOT NULL DEFAULT current_timestamp(),
	`updated_at` DATETIME NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
	PRIMARY KEY (`board_id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=9
;


```
faq 테이블
```

CREATE TABLE faq (
  id BIGINT NOT NULL AUTO_INCREMENT,
  category VARCHAR(100) NOT NULL,
  question_title VARCHAR(255) NOT NULL,
  answer TEXT NOT NULL,
  tags_json JSON NULL,
  owner_team VARCHAR(100) NOT NULL DEFAULT 'IT운영팀',
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  embedding_json LONGTEXT NULL,
  embedding_model VARCHAR(64) NULL,
  embedded_at DATETIME NULL,

  PRIMARY KEY (id),
  INDEX idx_faq_category (category),
  INDEX idx_faq_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


```
faq_chat_message 테이블
```

CREATE TABLE faq_chat_message (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id VARCHAR(50) NOT NULL,
  role VARCHAR(10) NOT NULL,      
  content TEXT NOT NULL,
  faq_id BIGINT NULL,             
  score DOUBLE NULL,              
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_created (user_id, created_at),
  INDEX idx_user_id (user_id, id)
);



```
faq 데이터 
```
INSERT INTO faq (category, question_title, answer, tags_json, owner_team)
VALUES
-- 1 ~ 12 로그인/접근
('로그인/접근','로그인을 하지 않으면 왜 공지를 볼 수 없나요?',
 '회사의 공지와 공유자료는 내부 정보이기 때문에 로그인한 사용자만 열람할 수 있도록 제한되어 있습니다. 인트라넷 로그인 후 공지 목록을 확인해 주세요.',
 JSON_ARRAY('로그인','공지','비회원','접근제한'),'IT운영팀'),

('로그인/접근','로그인했는데 공지가 보이지 않아요',
 '로그인은 정상적으로 되었으나 사용자 권한 또는 부서 정보가 아직 반영되지 않았을 수 있습니다. 새로고침 후에도 동일한 경우 IT운영팀에 문의해 주세요.',
 JSON_ARRAY('로그인','공지안보임','권한'),'IT운영팀'),

('로그인/접근','특정 부서 공지가 보이지 않아요',
 '부서 공지는 해당 부서에 소속된 사용자만 열람할 수 있습니다. 현재 소속 부서 정보가 올바른지 확인해 주세요.',
 JSON_ARRAY('부서공지','권한','접근'),'IT운영팀'),

('로그인/접근','세션이 자주 만료돼요',
 '보안 정책에 따라 일정 시간 동안 활동이 없을 경우 자동으로 로그아웃됩니다. 다시 로그인해 주세요.',
 JSON_ARRAY('세션만료','자동로그아웃'),'IT운영팀'),

('로그인/접근','자동 로그아웃은 왜 발생하나요?',
 '보안 강화를 위해 일정 시간 동안 사용자의 활동이 없으면 자동 로그아웃이 발생합니다.',
 JSON_ARRAY('자동로그아웃','보안'),'IT운영팀'),

('로그인/접근','회사 외부에서도 인트라넷에 접속할 수 있나요?',
 '회사 외부에서도 접속은 가능하지만, 일부 기능은 사내망 또는 VPN 연결이 필요할 수 있습니다.',
 JSON_ARRAY('외부접속','VPN','사내망'),'IT운영팀'),

('로그인/접근','VPN 없이도 공지를 볼 수 있나요?',
 '공지 열람은 VPN 없이도 가능하지만, 내부 전용 자료는 VPN 연결이 필요할 수 있습니다.',
 JSON_ARRAY('VPN','공지열람'),'IT운영팀'),

('로그인/접근','모바일에서도 공지를 확인할 수 있나요?',
 '모바일 브라우저에서도 공지 확인은 가능하지만, 일부 화면은 PC 환경에 최적화되어 있습니다.',
 JSON_ARRAY('모바일','공지','접속'),'IT운영팀'),

('로그인/접근','브라우저를 바꾸면 로그인이 풀려요',
 '브라우저별로 세션 정보가 관리되기 때문에 다른 브라우저로 접속하면 다시 로그인이 필요합니다.',
 JSON_ARRAY('브라우저','로그인','세션'),'IT운영팀'),

('로그인/접근','로그인했는데 권한이 없다고 나와요',
 '해당 공지 또는 게시글에 대한 접근 권한이 없는 경우 발생합니다. 권한 상태를 확인해 주세요.',
 JSON_ARRAY('권한없음','접근불가'),'IT운영팀'),

('로그인/접근','이전에 보이던 공지가 갑자기 안 보여요',
 '공지의 공개 범위가 변경되었거나 삭제되었을 수 있습니다. 공지 작성 부서에 확인해 주세요.',
 JSON_ARRAY('공지안보임','공개범위'),'IT운영팀'),

('로그인/접근','계정이 비활성화되었다고 표시돼요',
 '비활성화된 계정은 공지 열람이 불가능합니다. 인사 또는 IT운영팀에 계정 상태를 문의해 주세요.',
 JSON_ARRAY('계정비활성','접근불가'),'IT운영팀'),

-- 13 ~ 26 공지 열람
('공지 열람','최신 공지는 어디에서 확인하나요?',
 '인트라넷 메인 화면 또는 공지 게시판 상단에서 최신 공지를 확인할 수 있습니다.',
 JSON_ARRAY('최신공지','공지목록'),'IT운영팀'),

('공지 열람','공지 목록은 어떤 기준으로 정렬되나요?',
 '공지 목록은 기본적으로 게시일 기준 최신순으로 정렬됩니다.',
 JSON_ARRAY('공지정렬','게시일'),'IT운영팀'),

('공지 열람','예전 공지도 다시 볼 수 있나요?',
 '공지 게시판의 검색 기능을 이용하면 이전에 게시된 공지도 확인할 수 있습니다.',
 JSON_ARRAY('이전공지','공지검색'),'IT운영팀'),

('공지 열람','특정 공지를 검색하려면 어떻게 하나요?',
 '공지 게시판 상단의 검색창에 제목 또는 키워드를 입력하면 관련 공지를 검색할 수 있습니다.',
 JSON_ARRAY('공지검색','키워드'),'IT운영팀'),

('공지 열람','공지 목록에는 있는데 클릭이 되지 않아요',
 '해당 공지에 대한 열람 권한이 없거나 공지 상태가 변경되었을 수 있습니다. 권한 또는 공지 공개 범위를 확인해 주세요.',
 JSON_ARRAY('공지클릭불가','권한'),'IT운영팀'),

('공지 열람','공지가 삭제된 건가요?',
 '공지 작성자가 게시글을 삭제했거나 공개 범위를 변경했을 수 있습니다. 작성 부서에 문의해 주세요.',
 JSON_ARRAY('공지삭제','공지상태'),'IT운영팀'),

('공지 열람','비공개 공지는 누가 볼 수 있나요?',
 '비공개 공지는 공지 작성 시 지정된 부서 또는 사용자만 열람할 수 있습니다.',
 JSON_ARRAY('비공개공지','열람권한'),'IT운영팀'),

('공지 열람','부서 공지와 전사 공지의 차이는 무엇인가요?',
 '전사 공지는 모든 직원이 열람 가능하며, 부서 공지는 해당 부서 소속 사용자만 열람할 수 있습니다.',
 JSON_ARRAY('부서공지','전사공지'),'IT운영팀'),

('공지 열람','공지를 읽었는지 확인할 수 있나요?',
 '일부 공지는 열람 여부가 기록되며, 공지 상세 화면에서 확인할 수 있습니다.',
 JSON_ARRAY('공지열람','읽음표시'),'IT운영팀'),

('공지 열람','읽지 않은 공지는 따로 표시되나요?',
 '읽지 않은 공지는 목록에서 강조 표시되거나 별도의 아이콘으로 구분됩니다.',
 JSON_ARRAY('미열람공지','읽음표시'),'IT운영팀'),

('공지 열람','첨부파일이 있는 공지는 어떻게 확인하나요?',
 '공지 상세 화면에서 첨부파일 목록을 확인하고 다운로드할 수 있습니다.',
 JSON_ARRAY('공지첨부','파일다운로드'),'IT운영팀'),

('공지 열람','공지를 북마크할 수 있나요?',
 '현재 북마크 기능은 제공되지 않으며, 추후 기능 개선 시 추가될 예정입니다.',
 JSON_ARRAY('공지북마크','기능문의'),'IT운영팀'),

('공지 열람','공지 링크를 다른 직원에게 공유해도 되나요?',
 '공지 링크 공유는 가능하지만, 열람 권한이 없는 사용자는 접근할 수 없습니다.',
 JSON_ARRAY('공지공유','링크'),'IT운영팀'),

('공지 열람','모바일에서 공지가 잘려서 보여요',
 '모바일 환경에서는 일부 화면이 축약되어 표시될 수 있습니다. 전체 내용은 PC 환경에서 확인해 주세요.',
 JSON_ARRAY('모바일공지','화면잘림'),'IT운영팀'),

-- 27 ~ 40 공지 작성
('공지 작성','공지는 누가 작성할 수 있나요?',
 '공지 작성은 관리자 또는 부서별 공지 작성 권한이 부여된 사용자만 가능합니다.',
 JSON_ARRAY('공지작성','권한','관리자'),'IT운영팀'),

('공지 작성','공지 작성 메뉴가 보이지 않아요',
 '공지 작성 권한이 없는 경우 작성 메뉴가 표시되지 않습니다. 권한 신청 후 다시 확인해 주세요.',
 JSON_ARRAY('공지작성','메뉴안보임','권한'),'IT운영팀'),

('공지 작성','부서 공지는 어떻게 작성하나요?',
 '공지 작성 화면에서 공지 유형을 ''부서 공지''로 선택한 후 게시하면 됩니다.',
 JSON_ARRAY('부서공지','공지작성'),'IT운영팀'),

('공지 작성','전사 공지를 올리려면 어떤 권한이 필요한가요?',
 '전사 공지는 관리자 또는 전사 공지 권한이 있는 사용자만 작성할 수 있습니다.',
 JSON_ARRAY('전사공지','권한'),'IT운영팀'),

('공지 작성','공지 작성 후 바로 게시되나요?',
 '공지 작성 후 게시 버튼을 누르면 즉시 게시됩니다. 다만 일부 공지는 승인 절차가 필요할 수 있습니다.',
 JSON_ARRAY('공지게시','승인'),'IT운영팀'),

('공지 작성','공지를 임시저장할 수 있나요?',
 '공지 작성 화면에서 임시저장 기능을 이용하면 게시하지 않고 내용을 저장할 수 있습니다.',
 JSON_ARRAY('공지임시저장','작성중'),'IT운영팀'),

('공지 작성','이미 게시된 공지를 수정할 수 있나요?',
 '게시된 공지는 작성자 또는 관리자 권한이 있는 경우에 한해 수정할 수 있습니다.',
 JSON_ARRAY('공지수정','권한'),'IT운영팀'),

('공지 작성','이미 게시된 공지를 삭제할 수 있나요?',
 '게시된 공지는 작성자 또는 관리자만 삭제할 수 있으며, 삭제 시 복구가 불가능합니다.',
 JSON_ARRAY('공지삭제','관리자'),'IT운영팀'),

('공지 작성','공지에 첨부파일을 추가하려면 어떻게 하나요?',
 '공지 작성 화면에서 첨부파일 추가 버튼을 클릭하여 파일을 업로드할 수 있습니다.',
 JSON_ARRAY('공지첨부','파일업로드'),'IT운영팀'),

('공지 작성','첨부파일 용량 제한이 있나요?',
 '첨부파일은 개별 파일당 최대 용량 제한이 있으며, 제한을 초과하면 업로드가 불가능합니다.',
 JSON_ARRAY('첨부파일','용량제한'),'IT운영팀'),

('공지 작성','공지에 이미지가 표시되지 않아요',
 '이미지 파일 형식이 지원되지 않거나 업로드 중 오류가 발생했을 수 있습니다. 파일 형식을 확인해 주세요.',
 JSON_ARRAY('공지이미지','업로드오류'),'IT운영팀'),

('공지 작성','공지 작성 중 오류가 발생했어요',
 '네트워크 오류 또는 세션 만료로 인해 발생할 수 있습니다. 다시 로그인 후 시도해 주세요.',
 JSON_ARRAY('공지오류','작성중오류'),'IT운영팀'),

('공지 작성','공지 작성 후 저장이 되지 않아요',
 '필수 입력 항목이 누락되었거나 파일 업로드 오류가 있을 수 있습니다. 입력 내용을 다시 확인해 주세요.',
 JSON_ARRAY('공지저장','입력오류'),'IT운영팀'),

('공지 작성','다른 부서 공지를 대신 작성할 수 있나요?',
 '다른 부서 공지는 해당 부서에 대한 권한이 있는 경우에만 작성할 수 있습니다.',
 JSON_ARRAY('부서공지','대리작성'),'IT운영팀'),

-- 41 ~ 50 게시글 요약
('게시글 요약','공지 요약은 어떻게 생성되나요?',
 '공지 요약은 게시글 내용을 기반으로 자동 생성되며, 핵심 내용을 중심으로 제공합니다.',
 JSON_ARRAY('공지요약','자동요약'),'IT운영팀'),

('게시글 요약','요약 내용이 원문과 다른데요?',
 '요약은 핵심 위주로 생성되기 때문에 표현이나 세부 내용이 원문과 다를 수 있습니다. 정확한 내용은 원문을 참고해 주세요.',
 JSON_ARRAY('요약차이','공지요약'),'IT운영팀'),

('게시글 요약','요약이 표시되지 않는 공지가 있어요',
 '요약은 일정 길이 이상의 공지에만 제공되며, 조건에 따라 생성되지 않을 수 있습니다.',
 JSON_ARRAY('요약안보임','공지요약'),'IT운영팀'),

('게시글 요약','공지 요약은 누가 볼 수 있나요?',
 '공지 요약은 해당 공지를 열람할 수 있는 사용자라면 모두 확인할 수 있습니다.',
 JSON_ARRAY('공지요약','열람권한'),'IT운영팀'),

('게시글 요약','요약 기능을 끌 수 있나요?',
 '현재 요약 기능은 기본 제공 기능으로 개별 사용자 설정을 통한 비활성화는 지원되지 않습니다.',
 JSON_ARRAY('요약설정','기능문의'),'IT운영팀'),

('게시글 요약','요약이 너무 짧게 나와요',
 '요약은 핵심 정보 위주로 간결하게 생성됩니다. 세부 내용은 원문 공지를 참고해 주세요.',
 JSON_ARRAY('공지요약','짧은요약'),'IT운영팀'),

('게시글 요약','요약이 너무 길게 표시돼요',
 '공지 길이와 내용에 따라 요약 분량이 달라질 수 있으며, 필요 시 원문을 직접 확인해 주세요.',
 JSON_ARRAY('공지요약','긴요약'),'IT운영팀'),

('게시글 요약','첨부파일 내용도 요약되나요?',
 '현재 요약 기능은 공지 본문만을 대상으로 하며, 첨부파일 내용은 포함되지 않습니다.',
 JSON_ARRAY('첨부파일','요약범위'),'IT운영팀'),

('게시글 요약','공지 요약 생성에 시간이 걸려요',
 '공지 요약은 자동 생성 과정이 포함되어 있어 최초 조회 시 잠시 시간이 소요될 수 있습니다.',
 JSON_ARRAY('요약지연','공지요약'),'IT운영팀'),

('게시글 요약','요약 내용이 최신 공지 내용과 다르게 보여요',
 '공지 수정 후 요약이 아직 갱신되지 않았을 수 있습니다. 잠시 후 다시 확인해 주세요.',
 JSON_ARRAY('요약갱신','공지수정'),'IT운영팀'),

-- 51 ~ 62 권한/공개범위
('권한/공개범위','우리 부서 공지는 누가 볼 수 있나요?',
 '부서 공지는 해당 부서에 소속된 사용자만 열람할 수 있습니다.',
 JSON_ARRAY('부서공지','열람권한'),'IT운영팀'),

('권한/공개범위','다른 부서 공지를 볼 수 없어요',
 '공지 공개 범위가 제한되어 있는 경우 다른 부서 사용자는 열람할 수 없습니다.',
 JSON_ARRAY('공지권한','접근불가'),'IT운영팀'),

('권한/공개범위','부서 이동 후 공지가 안 보여요',
 '부서 이동 정보가 아직 시스템에 반영되지 않았을 수 있습니다. 반영까지 시간이 걸릴 수 있습니다.',
 JSON_ARRAY('부서이동','권한반영'),'IT운영팀'),

('권한/공개범위','권한 변경은 언제 반영되나요?',
 '권한 변경은 승인 완료 후 일정 시간 내에 시스템에 반영됩니다.',
 JSON_ARRAY('권한변경','반영시간'),'IT운영팀'),

('권한/공개범위','특정 사용자에게만 공지를 보여줄 수 있나요?',
 '공지 작성 시 공개 범위를 특정 사용자 또는 부서로 설정할 수 있습니다.',
 JSON_ARRAY('공지공개범위','사용자지정'),'IT운영팀'),

('권한/공개범위','공지 공개 범위는 어떻게 설정하나요?',
 '공지 작성 화면에서 공개 범위를 전사 또는 부서 단위로 설정할 수 있습니다.',
 JSON_ARRAY('공지공개','범위설정'),'IT운영팀'),

('권한/공개범위','퇴사자는 공지를 볼 수 있나요?',
 '퇴사 처리된 계정은 비활성화되며, 공지 열람이 불가능합니다.',
 JSON_ARRAY('퇴사자','계정비활성'),'IT운영팀'),

('권한/공개범위','입사자는 언제부터 공지를 볼 수 있나요?',
 '입사자 계정이 활성화되고 권한이 부여되면 즉시 공지 열람이 가능합니다.',
 JSON_ARRAY('입사자','공지열람'),'IT운영팀'),

('권한/공개범위','관리자 권한은 누가 부여하나요?',
 '관리자 권한은 IT운영팀 또는 시스템 관리자에 의해 부여됩니다.',
 JSON_ARRAY('관리자권한','권한부여'),'IT운영팀'),

('권한/공개범위','권한 신청은 어디서 하나요?',
 '인트라넷 내 권한 신청 메뉴를 통해 권한을 요청할 수 있습니다.',
 JSON_ARRAY('권한신청','권한요청'),'IT운영팀'),

('권한/공개범위','권한 승인 상태는 어디서 확인하나요?',
 '권한 승인 상태는 인트라넷 내 권한 신청 내역 화면에서 확인할 수 있습니다.',
 JSON_ARRAY('권한승인','신청상태'),'IT운영팀'),

('권한/공개범위','권한이 있는데도 접근이 안 돼요',
 '권한 변경 사항이 아직 시스템에 반영되지 않았거나 세션이 갱신되지 않았을 수 있습니다. 로그아웃 후 다시 로그인해 보세요.',
 JSON_ARRAY('권한오류','접근불가'),'IT운영팀'),

-- 63 ~ 80 오류/장애/문의
('오류/장애','공지 클릭 시 403 오류가 발생해요',
 '403 오류는 해당 공지에 대한 접근 권한이 없을 때 발생합니다. 로그인 상태와 권한을 확인해 주세요.',
 JSON_ARRAY('403오류','권한'),'IT운영팀'),

('오류/장애','공지 클릭 시 401 오류가 발생해요',
 '401 오류는 인증이 필요한 상태에서 접근할 때 발생합니다. 다시 로그인해 주세요.',
 JSON_ARRAY('401오류','인증'),'IT운영팀'),

('오류/장애','공지 페이지가 하얗게 나와요',
 '브라우저 캐시 문제 또는 일시적인 오류일 수 있습니다. 새로고침 또는 캐시 삭제 후 다시 시도해 주세요.',
 JSON_ARRAY('화면오류','하얀화면'),'IT운영팀'),

('오류/장애','공지 페이지에서 무한 로딩이 발생해요',
 '네트워크 상태 불안정 또는 서버 응답 지연으로 발생할 수 있습니다. 잠시 후 다시 시도해 주세요.',
 JSON_ARRAY('무한로딩','공지오류'),'IT운영팀'),

('오류/장애','첨부파일 다운로드가 되지 않아요',
 '파일 권한 문제 또는 네트워크 오류일 수 있습니다. 다른 브라우저에서 다시 시도해 주세요.',
 JSON_ARRAY('첨부파일','다운로드오류'),'IT운영팀'),

('오류/장애','화면이 깨져서 보여요',
 '브라우저 호환성 문제일 수 있습니다. 최신 버전의 브라우저 사용을 권장합니다.',
 JSON_ARRAY('화면깨짐','브라우저'),'IT운영팀'),

('오류/장애','새로고침하면 공지가 사라져요',
 '세션 만료 또는 권한 변경으로 인해 발생할 수 있습니다. 다시 로그인해 주세요.',
 JSON_ARRAY('공지사라짐','세션'),'IT운영팀'),

('오류/장애','브라우저 캐시는 어떻게 삭제하나요?',
 '브라우저 설정 메뉴에서 캐시 및 쿠키 삭제 기능을 이용해 주세요.',
 JSON_ARRAY('캐시삭제','브라우저'),'IT운영팀'),

('오류/장애','크롬에서만 문제가 발생해요',
 '브라우저 확장 프로그램 또는 설정 문제일 수 있습니다. 확장 프로그램을 비활성화해 보세요.',
 JSON_ARRAY('크롬오류','브라우저'),'IT운영팀'),

('오류/장애','엣지에서만 문제가 발생해요',
 '엣지 브라우저의 캐시 또는 설정 문제일 수 있습니다. 캐시 삭제 후 다시 시도해 주세요.',
 JSON_ARRAY('엣지오류','브라우저'),'IT운영팀'),

('오류/장애','사내망에서만 접속되나요?',
 '일부 내부 전용 기능은 사내망 또는 VPN 환경에서만 이용 가능합니다.',
 JSON_ARRAY('사내망','VPN'),'IT운영팀'),

('오류/장애','시스템 점검 시간은 어디서 확인하나요?',
 '시스템 점검 일정은 인트라넷 공지사항을 통해 사전에 안내됩니다.',
 JSON_ARRAY('시스템점검','공지'),'IT운영팀'),

('오류/장애','시스템 장애 공지는 어디서 확인하나요?',
 '시스템 장애 발생 시 인트라넷 공지 게시판을 통해 안내됩니다.',
 JSON_ARRAY('시스템장애','공지'),'IT운영팀'),

('오류/장애','오류 발생 시 어떤 정보를 전달해야 하나요?',
 '오류 문의 시에는 발생 시간, 오류 메시지 또는 코드, 사용 중이던 기능, 화면 캡처 정보를 함께 전달해 주시면 문제 해결에 도움이 됩니다.',
 JSON_ARRAY('오류문의','정보전달','스크린샷'),'IT운영팀'),

('오류/장애','IT 담당 부서는 어디인가요?',
 '인트라넷 시스템 관련 문의는 IT운영팀에서 담당하고 있습니다. 인트라넷 내 문의 채널을 통해 연락해 주세요.',
 JSON_ARRAY('IT운영팀','문의처'),'IT운영팀'),

('오류/장애','긴급 공지는 어떻게 확인하나요?',
 '긴급 공지는 인트라넷 메인 화면 상단 또는 공지 게시판 상단에 우선적으로 표시됩니다.',
 JSON_ARRAY('긴급공지','시스템공지'),'IT운영팀'),

('오류/장애','문의에 대한 답변은 얼마나 걸리나요?',
 '일반 문의는 접수 후 영업일 기준 1~2일 이내에 답변을 드리고 있으며, 긴급 장애의 경우 우선적으로 처리됩니다.',
 JSON_ARRAY('문의응답','처리시간'),'IT운영팀'),

('오류/장애','챗봇이 답변을 못 할 때는 어떻게 하나요?',
 '챗봇이 정확한 답변을 제공하지 못하는 경우 IT운영팀에 직접 문의해 주세요. 해당 질문은 이후 챗봇 학습 및 FAQ 개선에 반영됩니다.',
 JSON_ARRAY('챗봇한계','문의안내'),'IT운영팀')
;
