# MCOPT - Minecraft Performance Optimization Mod

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1-green.svg)](https://www.minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.77-orange.svg)](https://neoforged.net/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

MCOPT is a performance optimization mod for Minecraft designed to improve client-side performance in singleplayer and multiplayer environments. It focuses on reducing lag through intelligent chunk rendering, entity culling, and particle system optimizations while maintaining full compatibility with other mods.

## 최근 변경점 (1.21.10 실험 빌드)
- **Idle Boost 유휴 감지 추가**: 입력이 일정 시간 없을 때 FPS와 렌더 거리를 낮춰 발열과 소음을 줄입니다.
- **파티클 거리/폭주 제한**: 카메라와 멀리 떨어진 파티클을 즉시 건너뛰고, 한 틱에 생성할 수 있는 파티클 양을 제한해 급격한 스파이크를 방지합니다.
- **꿀벌 복귀 안정화**: 둥지 주변에서 길을 잃은 벌이 오래 맴돌면 목표를 초기화해 경로 탐색 렉을 줄입니다.
- **아이템 데이터 무결성 보강**: `ItemStack.copy()` 시 NBT를 강제로 깊은 복사하여 포탈 이동·전송 중 데이터 손상 확률을 낮춥니다.

## Features

### 🎮 Client-Side Optimizations

#### Chunk Rendering Optimization
- **Smart Chunk Update Limiting**: Prevents frame drops by limiting the number of chunk updates per frame
- **Aggressive Chunk Culling**: Optional advanced culling for maximum FPS (may cause slight pop-in)
- **Frustum Calculation Caching**: Reduces CPU overhead from redundant calculations
- **Elliptical Render Distance** ⭐ NEW: Renders chunks in a 3D ellipsoid instead of square/cylinder
  - Reduces rendered chunk sections by 10-35% for significant FPS improvement
  - Configurable vertical and horizontal stretch factors
  - Maintains visual quality while improving performance
  - Optional debug overlay showing culled chunk count

#### Entity Rendering Optimization
- **Distance-Based Entity Culling**: Automatically skips rendering distant entities
- **Behind-Wall Culling**: Optionally culls entities that are completely behind walls
- **Smart Importance Detection**: Never culls important entities like vehicles or passengers

#### Block Entity Rendering Optimization ⭐ NEW
- **Distance-Based Block Entity Culling**: Skips rendering distant block entities (chests, signs, skulls)
- **Behind-Wall Culling**: Optionally culls block entities that are behind walls
- **Major FPS Boost in Storage Rooms**: Significant performance improvement in large warehouses
- **Configurable Distance**: Adjust culling distance based on your render distance

#### Dynamic FPS 컨트롤러 ⭐ NEW
- **창 상태 기반 FPS 캡**: 플레이 화면, 메뉴, 비활성화, 최소화 상태마다 서로 다른 FPS 제한 적용
- **원본 값 보존**: 사용자가 지정한 최대 FPS를 기억했다가 포커스를 되찾으면 즉시 복원
- **완전 독립 구현**: 렌더 스레드의 프레임 한도만 건드려 다른 모드/루프와 충돌 최소화
- **백그라운드 스로틀링 토글**: `enableBackgroundThrottling`으로 녹화/방송 시 백그라운드에서도 풀 프레임 유지 가능
- **중복 기능 자동 비활성화**: `dynamic_fps` 또는 `fps_reducer` 모드 감지 시 내장 컨트롤러를 자동으로 끔
- **유휴 입력 감지 (Idle Boost)** ⭐ NEW: 입력이 일정 시간 없을 때 FPS를 낮춰 발열과 소음을 줄임
  - 기본 20초 무입력 시 발동, 다시 조작하면 즉시 원래 한도로 복구
  - 게임 내 화면에서만 동작해 메뉴 탐색이나 백그라운드 작업과 충돌하지 않음
  - `idleInactivitySeconds`와 `idleFrameRateLimit`로 민감도/제한값을 자유롭게 조절

#### Particle System Optimization
- **Per-Frame Particle Limiting**: Prevents FPS drops from particle explosions
- **Probabilistic Spawn Reduction**: Maintains visual quality while reducing particle count
- **Distance-Based Particle Culling**: Skips particles that are too far from the camera
- **Particle Occlusion Culling** ⭐ NEW: 보이는 파티클만 그려서 GPU 낭비를 줄임
  - 카메라와 파티클 사이에 불투명 블록이 있으면 렌더를 건너뜀
  - 기본 3프레임마다 시야 차단을 재검사해 과도한 레이캐스트를 방지
  - `particles.particleCullingRange`로 검사 거리, `particles.particleOcclusionCheckInterval`로 재검사 주기를 조절

#### Smart Leaves Culling ⭐ NEW
- **OptiLeaves 스타일 최적화**: 나무 안쪽의 보이지 않는 나뭇잎 면을 컬링하여 GPU 부담 감소
- **숲 바이옴 FPS 향상**: 정글, 다크 오크 숲에서 10-40% 성능 향상
- **시각적 품질 유지**: Fancy 그래픽 모드의 외형을 그대로 유지하면서 내부 면만 제거
- **자동 호환성 보호**: cull-leaves, moreculling, optileaves, cull-less-leaves 모드 감지 시 자동 비활성화
- **깊이 기반 컬링**: 설정 가능한 depth 값으로 나무가 투명해 보이는 것을 방지

#### Snow Accumulation Optimization ⭐ NEW
- **Simple Snowy Fix 스타일**: 눈 층이 늘어날 때 불필요한 이웃 알림을 줄여 눈보라 시 청크 리빌드 스파이크 감소
- **Vanilla 호환**: 눈 쌓이는 방식은 그대로 유지하면서 업데이트 플래그만 최소화
- **바닐라 토글 제공**: `enableBetterSnowLogic`을 끄면 원본 눈 적층 방식 그대로 사용

#### 꿀벌 둥지 복귀 안정화 (NeoBeeFix 영감) ⭐ NEW
- **둥지-꽃 동기화 강화**: 오랫동안 둥지로 돌아가지 못하는 꿀벌의 목표 좌표를 자동 초기화
- **경로 재탐색 지연 페널티**: 막힌 둥지를 반복 탐색해 틱 시간을 잡아먹는 현상을 완화
- **완전 독립 구현**: 네비게이션 진행 상황만 추적하므로 다른 벌 관련 모드와 충돌을 최소화
- **Config 토글 제공**: `bee_fix.enableBeeStuckFix`로 필요 시 간단히 끌 수 있음

#### Experience Orb Merging Optimization ⭐ NEW
- **Automatic Orb Merging**: Combines nearby experience orbs into single entities
- **Configurable Merge Radius**: Adjust how aggressively orbs merge
- **Performance Boost**: Significantly reduces lag during mob farming or mining
- **충돌 회피**: Clumps 모드 감지 시 자동으로 비활성화해 중복 병합을 방지

#### 맞춤 Clear Lag 스케줄러 ⭐ NEW
- **주기적 지상 엔티티 정리**: 일정 시간마다 아이템/경험치/투사체를 정리해 버려진 엔티티로 인한 렉을 완화
- **경고 브로드캐스트**: 정리 직전에 알림을 보내 플레이어가 소지품을 회수할 시간을 제공
- **화이트리스트/보호 옵션**: 이름표가 붙은 아이템이나 지정한 엔티티 ID는 건드리지 않도록 설정 가능
- **온화한 기본값**: 기본적으로 꺼져 있으며, 필요할 때만 활성화하도록 설계

#### 청크 당 엔티티 제한 (Per-Chunk Entity Limiter) ⭐ NEW
- **국지적 렉 방지**: 특정 청크에 엔티티가 과도하게 밀집되는 것을 실시간으로 방지
- **몹 타워 보호**: 몹 농장이나 자동화 시설에서 발생하는 국지적인 FPS 저하를 완화
- **선택적 제한**: 몬스터, 동물, 아이템 등 엔티티 타입별로 제한 대상 설정 가능
- **스폰 차단 또는 제거**: 청크가 꽉 찼을 때 새로운 스폰을 막거나 초과분을 제거하는 방식 선택 가능
- **Clear Lag와 상호 보완**: Clear Lag는 전역 정리, 이 기능은 청크별 실시간 방어

#### Iron Golem Spawn 안정화 ⭐ NEW
- **Villager 소환 보정**: 마을 주민이 소환하는 철 골렘의 스폰 위치를 주변 지면으로 부드럽게 내림
- **지붕/장식물 우회**: 높이맵 때문에 지붕 위나 장식 블록에 걸려 스폰이 무산되는 문제 완화
- **검색 깊이 조절**: 설정을 통해 얼마나 아래로 안전한 지면을 찾을지 선택 가능

#### 포탈 탑승자 동기화 ⭐ NEW
- **동승자 보호**: 말을 탄 플레이어, 보트/광산수레 승객이 네더·엔드 포탈을 통과할 때 함께 이동
- **차량 우선 처리 대응**: 포탈 판정이 탈것에만 적용되는 바닐라 로직을 보완해 탑승객이 낙오되지 않음
- **호환성 우선**: 포탈 처리 흐름만 보강해 다른 포탈/차원 관련 모드와 충돌을 최소화

#### 포탈 리다이렉트 (Redirected 영감) ⭐ NEW
- **포탈 귀환 기억**: 차원마다 마지막으로 사용한 포탈 좌표를 저장해 되돌아올 때 같은 포탈로 배정
- **무차별 링크 방지**: 가까운 다른 포탈로 튕기는 바닐라 경향을 줄여 멀티포탈 거점에서도 안정적인 이동 보장
- **경량 구현**: Mixin 한 곳에서 입구만 기록하고 도착 시 살짝 재배치하는 방식으로 타 모드와의 충돌 여지 최소화

#### dontDoThat 스타일 안전장치 ⭐ NEW
- **길들여진 애완동물 보호**: 늑대·고양이·앵무새 등 길들여진 동물을 실수로 때리면 공격을 취소하고 경고를 표시
- **주민/행상인 보호**: 우호적인 마을 주민을 때릴 때 한 번 더 확인해 갑작스런 평판 하락을 방지
- **장식물 보호**: 아이템 액자나 그림이 한 방에 부서지지 않도록 막아두고, 원할 경우 웅크린 상태로만 파괴 허용
- **호환성 우선**: 기존 dontDoThat 모드가 설치되어 있으면 자동으로 비활성화해 중복 기능을 피함

#### 낚싯대 동기화 복구 (Fishing Rod Fix 영감) ⭐ NEW
- **고아 낚싯찌 정리**: 낚싯대가 사라지거나 차원을 이동한 뒤 남아버린 낚싯찌를 자동으로 제거해 "낚싯대 먹통" 상태를 해소
- **거리/청크 안전 검사**: 플레이어와 1024블록 이상 떨어지거나 언로드된 청크에 남은 낚싯찌를 감지해 줄을 정리
- **완전 서버 사이드**: 로직을 건드리지 않고 참조만 정리해 다른 낚시 확장 모드와도 충돌 없이 동작

#### 로그인 타임아웃 방지 (Login Timeout Fix) ⭐ NEW
- **무거운 모드팩 지원**: 클라이언트가 서버에 접속할 때 로딩 시간이 오래 걸려 튕기는 문제 해결
- **설정 가능한 타임아웃**: 바닐라 30초 대신 최대 600초(10분)까지 설정 가능
- **권장 설정**: 무거운 모드팩의 경우 120-180초 권장
- **"Timed Out" 에러 방지**: 모드팩이 많을수록 로딩 시간이 길어지는 문제를 근본적으로 해결

#### 월드 생성 안정화 (Lake Crash Fix 스타일) ⭐ NEW
- **호수 생성 크래시 방지**: 커스텀 지형 생성 중 호수 기능이 로드되지 않은 청크의 바이옴을 체크할 때 발생하는 크래시 방지
- **청크 로드 확인**: 바이옴 체크 전에 해당 위치의 청크가 로드되어 있는지 안전하게 확인
- **안전한 예외 처리**: 청크가 로드되지 않았거나 예외가 발생하면 안전한 기본값으로 처리
- **모드팩 호환성**: 커스텀 월드 생성 모드와 함께 사용할 때 안정성 향상
- **완전 독립 구현**: Lake Feature Fix 모드에서 영감을 받았지만 MCOPT 자체 구현

#### Basin 생성 복원 (Basin Generation Fix 스타일) ⭐ NEW
- **돌 디스크 피처 복원**: Plains, Forest, Savanna 등의 오버월드 바이옴에서 Basin (돌 디스크) 생성을 복원
- **자연스러운 지형**: 바닐라 Minecraft에서 누락되었던 자연스러운 돌 지형 피처를 재추가
- **설정 가능**: GameplayConfig에서 enableBasinFix로 쉽게 켜고 끌 수 있음
- **BiomeModifier 기반**: NeoForge의 BiomeModifier 시스템을 사용한 깔끔한 구현
- **완전 독립 구현**: Basin Generation Fix 모드에서 영감을 받았지만 MCOPT 자체 구현

#### 버킷 미리보기 ⭐ NEW
- **버킷 내용 툴팁**: 버킷에 담긴 액체나 생명체 정보를 툴팁에 표시해 이름만으로는 구분하기 어려운 경우를 해소
- **희귀 변이 식별**: 열대어 패턴/색상 조합, 아홀로틀 변종 색상을 바로 보여 레어 물고기나 파란 아홀로틀을 놓치지 않음
- **완전 클라이언트 사이드**: 시각 정보만 추가하므로 서버 권한 없이도 안전하게 사용 가능, 다른 모드 버킷에도 대응

#### 상호작용 폴스루 (RCF 스타일) ⭐ NEW
- **오른손 실패 시 왼손 자동 시도**: 오른손 아이템 사용이 실패(예: 공간 부족)했을 때 자동으로 왼손 아이템 사용을 시도
- **블록 설치 편의성 향상**: 오른손에 블록이 있지만 설치할 공간이 없을 때 왼손의 블록을 자동으로 사용
- **클라이언트/서버 양쪽 지원**: 싱글플레이어와 멀티플레이어 모두에서 동작
- **완전 독립 구현**: RCF 모드에서 영감을 받았지만 MCOPT 자체 구현

#### 아이템 액자 소음 방지 (BugFixerUpper 스타일) ⭐ NEW
- **청크 로딩 시 불필요한 소리 제거**: 청크가 로드될 때 아이템 액자가 재생하는 배치 소리를 음소거
- **플레이어 설치 시 정상 작동**: 플레이어가 직접 아이템 액자를 설치할 때는 정상적으로 소리가 재생됨
- **탐험 경험 개선**: 아이템 액자가 많은 지역을 탐험할 때 불필요한 소음으로 인한 불편함 해소
- **완전 독립 구현**: BugFixerUpper 모드에서 영감을 받았지만 MCOPT 자체 구현

#### 수영 상태 동기화 수정 (Swim Fix 스타일) ⭐ NEW
- **MC-220390 버그 수정**: 물 속에서 수영 중 공격 시 발생하는 클라이언트-서버 히트박스 불일치 해결
- **해저 신전 전투 개선**: Ocean Monument에서 가디언과 싸울 때 수영 상태가 풀리는 문제 방지
- **1x1 공간 글리치 해결**: 수영 중 공격 후 좁은 공간을 통과할 때 발생하던 글리치 수정
- **자동 상태 복원**: 공격 직후 수영 조건이 충족되면 자동으로 수영 상태를 복원
- **서버 측 처리**: 서버에서 동기화 문제를 해결하여 멀티플레이어에서도 안정적으로 작동
- **완전 독립 구현**: Swim Fix 모드에서 영감을 받았지만 MCOPT 자체 구현

#### 마법 부여 시드 동기화 강화 (Enchanter Fix 스타일) ⭐ NEW
- **진짜 무작위화**: 테이블 슬롯(재료/라피스)이 바뀔 때마다 마법 부여 시드를 새로 뽑아 예측을 어렵게 만듭니다
- **즉시 동기화**: 시드 변경 직후 `broadcastChanges()`를 호출하여 클라이언트가 항상 올바른 마법 부여 옵션을 표시하도록 보장
- **바닐라 버그 수정**: 클라이언트에 오래된 마법 부여 옵션이 표시되는 바닐라 동기화 문제 해결
- **간단한 설정 토글**: `fixEnchantmentRNG` 옵션으로 기존 바닐라식(플레이어 고정 시드)과 자유롭게 전환
- **완전 독립 구현**: Enchanter Fix 모드의 문제 해결 방식에서 영감을 받았지만, MCOPT만의 독자적인 Mixin 패턴으로 구현
- **비침습적 구현**: UI/요구 레벨 등 원본 동작은 그대로 유지한 채 난수와 동기화만 개선해 다른 모드와의 충돌을 최소화

#### Dynamic Memory Management ⭐ NEW
- **GC Spike Prevention**: Object pooling for Vec3 and BlockPos to reduce garbage collection pressure
- **Smart Resource Cleanup**: Automatic cleanup of unused assets on world unload/disconnect
- **Memory HUD**: Real-time RAM usage display in top-left corner
- **Panic Button (F8)**: Emergency memory cleanup with instant feedback
- **FerriteCore Compatible**: Designed to complement static memory optimizations

#### Leak Guard (AllTheLeaks 스타일) ⭐ NEW
- **월드 전환 메모리 누수 감시**: 언로드된 월드 참조가 남아있는지 실시간 감시
- **경량 GC 보조 옵션**: 필요 시 단 한 번의 `System.gc()`로 정체된 참조 해제 시도
- **메모리 사용량 경고**: 설정된 임계치 이상 사용 시 경고 로그 출력
- **안전 청소**: 실행 중인 스레드를 건드리지 않고 유휴 상태에서만 캐시를 정리
- **MCOPT 캐시 초기화**: 월드 언로드 시 자체 캐시를 즉시 비워 누수 위험 최소화
- **타 모드 감지**: AllTheLeaks/MemoryLeakFix가 설치되어 있으면 자동 비활성화

#### Max Health 안정화 ⭐ NEW
- **비율 기반 체력 유지**: MAX_HEALTH 속성이 변해도 현재 체력 비율을 유지
- **로그인/차원 이동 보호**: 추가 하트가 있는 플레이어가 10하트로 초기화되는 문제 방지
- **버프 만료 완충**: 임시 버프가 끝나도 갑작스런 대미지 없이 체력을 부드럽게 조정

#### 속성 한도 확장 (AttributeFix 스타일) ⭐ NEW
- **상한 해제형 클램프**: 바닐라 1024 상한을 10억으로 확장해 대형 모드팩의 장비/스탯이 잘리지 않음
- **보고 값 일치**: 속성의 최대치 조회 시에도 확장된 값이 반환되어 UI/호환 모드가 올바른 범위를 인식
- **토글 가능**: 다른 모드가 고정 상한을 필요로 할 경우 설정에서 즉시 끌 수 있음

#### 포션 레벨 제한 해제 (PotionLimitFix 스타일) ⭐ NEW
- **Amplifier Byte Overflow 수정**: 포션 효과 레벨이 128 이상일 때 발생하는 byte overflow 버그 해결
- **NBT 확장 저장**: 레벨 128 이상의 포션 효과를 int로 저장하여 음수 변환 방지
- **바닐라 버그 해결**: Haste 128레벨이 블록 채굴을 불가능하게 만들거나, Levitation이 중력을 증가시키는 문제 수정
- **레벨 표시 개선**: 10 초과 레벨을 로마 숫자 대신 아라비아 숫자로 표시 (예: "Speed II" → "Speed 11")
- **하위 호환성**: 기존 바닐라 byte 저장 방식과 호환되며, 확장된 레벨만 추가 태그로 저장
- **모드팩 친화적**: 극한 포션 효과를 사용하는 모드팩에서 안정적으로 작동

#### 즉시 기상 시간 동기화 (InstantWakeupFix) ⭐ NEW
- **즉각적인 화면 전환**: 모든 플레이어가 침대에서 일어날 때 시간 변경을 즉시 클라이언트에 동기화
- **딜레이 제거**: 바닐라는 20틱마다 시간 패킷을 전송하여 최대 1초 딜레이 발생, 이를 완전히 제거
- **매끄러운 사용자 경험**: 밤→낮 전환이 즉시 화면에 반영되어 몰입감 향상
- **서버 친화적**: 패킷 하나만 추가 전송하므로 네트워크 부하 최소화
- **완전 독립 구현**: Instant Sky 모드에서 영감을 받았지만 MCOPT 자체 Mixin 패턴으로 구현

#### Allay 영속성 보장 (AllayPersistenceFix) ⭐ NEW
- **아이템 보유 시 디스폰 방지**: Allay가 손에 아이템을 들고 있을 때 디스폰되지 않도록 보장
- **작업 중단 방지**: 플레이어가 아이템 수집을 지시한 Allay가 사라지는 문제 해결
- **안정적인 아이템 수집**: 장거리 이동 중에도 Allay가 아이템을 안전하게 운반
- **완전 독립 구현**: AllayFix 모드에서 영감을 받았지만 MCOPT 자체 Mixin 패턴으로 구현

#### 포션 스택 시스템 (PotionStackingSystem) ⭐ NEW
- **포션 스택 가능**: 물병, 일반 포션, 투척용 포션, 잔류형 포션을 최대 16개까지 스택 가능
- **인벤토리 절약**: 포션 보관 공간을 1/16로 줄여 인벤토리 관리 편의성 대폭 향상
- **설정 가능**: 스택 크기를 1-64 사이로 자유롭게 조절
- **완전 독립 구현**: Potion Fixes 모드에서 영감을 받았지만 MCOPT 자체 구현
- **주의사항**: 양조기에서는 스택된 포션을 1개씩 분리하여 사용 권장

#### 조용한 번개 (SilentLightningFix) ⭐ NEW
- **Silent 태그 지원**: 번개에 Silent 태그가 있으면 소리를 재생하지 않음
- **명령어 호환**: `/summon minecraft:lightning_bolt ~ ~ ~ {Silent:1b}` 명령어 지원
- **맵 제작자 친화적**: 커스텀 맵이나 데이터팩에서 조용한 번개 효과 사용 가능
- **완전 독립 구현**: SilentLightningFix 모드에서 영감을 받았지만 MCOPT 자체 구현

#### Entity AI Optimization ⭐ NEW
- **Math Function Caching**: Pre-computed atan2, sin, cos lookup tables for AI calculations
- **Optimized LookControl**: Replaces mob LookControl with cached math version
- **Selective AI Goal Removal**: Configure which AI behaviors to disable per mob type
  - Common goals: LookAtPlayer, RandomLookAround
  - Animal behaviors: Float, Panic, Breed, Tempt, FollowParent, Stroll
  - Sheep-specific: EatBlock (wool regrowth)
  - Aquatic mobs: Swimming, Panic, Flee behaviors
- **Performance Scaling**: Greater improvements with more mobs (100+ entities)
- **호환성 우선**: 보스/주민은 건드리지 않고, AI-Improvements 모드가 설치되면 자동으로 비활성화
- **Inspired by AI-Improvements**: Independent implementation with Mixin-based injection

### ⚙️ Highly Configurable

All optimizations can be toggled and fine-tuned through the mod's configuration file located at `.minecraft/config/mcopt-client.toml`.

## Installation

1. Download and install [NeoForge](https://neoforged.net/) for Minecraft 1.21.1
2. Download the latest MCOPT release from the [Releases](https://github.com/randomstrangerpassenger/MCOPT/releases) page
3. Place the downloaded `.jar` file in your `.minecraft/mods` folder
4. Launch Minecraft with the NeoForge profile

## Configuration

After the first launch, a configuration file will be created at `.minecraft/config/mcopt-client.toml`. You can edit this file to customize the optimization settings.

### Configuration Options

#### Dynamic FPS
```toml
[general.dynamic_fps]
# 게임 화면이 아닌 상황에서 FPS를 자동으로 낮춰 CPU/GPU 사용량을 줄입니다
enableDynamicFps = true

# 백그라운드에서도 풀 FPS를 유지할지 여부 (녹화/방송 시 true -> false 권장)
enableBackgroundThrottling = true

# 메뉴나 일시정지 화면에서의 FPS 제한 (0은 무제한)
menuFrameRateLimit = 30

# 창 포커스를 잃었을 때의 FPS 제한 (0은 무제한)
unfocusedFrameRateLimit = 15

# 창이 최소화되었을 때의 FPS 제한 (0은 무제한)
minimizedFrameRateLimit = 1

# 일정 시간 입력이 없을 때 FPS를 낮춰 배터리/발열을 줄입니다
enableIdleBoost = true

# 몇 초 동안 입력이 없으면 유휴 상태로 간주할지 설정합니다
idleInactivitySeconds = 20

# 유휴 상태일 때 적용할 FPS 제한 (0은 무제한)
idleFrameRateLimit = 10
```

#### Chunk Rendering
```toml
[general.chunk_rendering]
# Enable chunk rendering optimizations
enableChunkOptimizations = true
# Maximum number of chunk updates per frame (1-20, default: 6)
chunkUpdateLimit = 6
# Enable aggressive chunk culling (may cause pop-in)
aggressiveChunkCulling = false
```

#### Elliptical Render Distance
```toml
[general.render_distance]
# Enable elliptical render distance optimization
enableEllipticalRenderDistance = true
# Vertical stretch factor (0.1-3.0, default: 0.75)
# Lower = better performance, higher = see more vertically
verticalRenderStretch = 0.75
# Horizontal stretch factor (0.5-2.0, default: 1.0)
# Values > 1.0 extend render distance horizontally
horizontalRenderStretch = 1.0
# Show debug overlay with culled chunk count
showCulledChunksDebug = false
```

#### Fishing rod stability
```toml
[general.fishing]
# 낚싯대/찌 참조가 어긋났을 때 자동으로 정리해 재투척을 가능하게 합니다
enableFishingRodFix = true
```

#### Entity Culling
```toml
[general.entity_culling]
# Enable entity culling optimizations
enableEntityCulling = true
# Distance at which entities are culled (16-256 blocks, default: 64)
entityCullingDistance = 64
# Skip rendering entities behind walls
cullEntitiesBehindWalls = true
```

#### Block Entity Culling
```toml
[rendering.block_entity_culling]
# Enable block entity culling optimizations (chests, signs, skulls, etc.)
enableBlockEntityCulling = true
# Distance at which block entities are culled (16-256 blocks, default: 64)
blockEntityCullingDistance = 64
# Skip rendering block entities behind walls
cullBlockEntitiesBehindWalls = true
```

#### Particle System
```toml
[general.particles]
# Enable particle system optimizations
enableParticleOptimizations = true
# Maximum particles per frame (100-4000, default: 500)
maxParticlesPerFrame = 500
# Reduce particle spawn rate (0.0-0.9, default: 0.25 = 25% reduction)
particleSpawnReduction = 0.25
```

#### Smart Leaves Culling
```toml
[rendering.smart_leaves]
# 나무 안쪽의 보이지 않는 나뭇잎 렌더링을 생략하여 숲 바이옴의 FPS를 높입니다
enableSmartLeaves = true

# 컬링을 적용할 최소 깊이 (0-5, 기본값: 2)
# 0 = 가장 공격적인 컬링 (최고 성능, 약간의 시각적 변화 가능)
# 2 = 권장 설정 (성능과 품질의 균형, Cull Less Leaves 스타일)
# 높을수록 = 덜 공격적인 컬링 (나무가 속이 빈 것처럼 보이는 것을 방지)
leavesCullingDepth = 2
```

#### Weather & Snow
```toml
[general.weather]
# 눈 층이 쌓일 때 불필요한 이웃 알림을 줄여 렌더링 스파이크 완화
enableSnowAccumulationFix = true
# 바닐라 눈 적층 방식을 유지하고 싶다면 false로 설정
enableBetterSnowLogic = true
```

#### Bee Stuck Fix
```toml
[general.bee_fix]
# 꿀벌이 둥지로 돌아가지 못하고 멈춰 있을 때 좌표를 초기화해 다시 탐색하게 합니다
enableBeeStuckFix = true

# 같은 둥지를 향해 진척 없이 버티는 최대 시간 (틱)
stuckTimeoutTicks = 200

# 둥지를 잃었을 때 다시 찾기 시작하기 전 최소 대기 시간 (틱)
relinkCooldownTicks = 40

# 잘못된 둥지를 반복 검색한 경우 적용할 추가 지연 (틱)
failedHiveSearchPenalty = 1200
```

#### Village
```toml
[general.villages]
# 마을 주민이 소환한 철 골렘을 주변 안전 지면으로 스냅해 실패율 감소
enableGolemSpawnFix = true
# 골렘 스폰 위치를 아래로 최대 몇 블록까지 검색할지 (1-32)
golemSpawnSearchRange = 6
```

#### Portals
```toml
[general.portals]
# 탈것이 먼저 포탈에 닿아도 탑승자가 함께 전송되도록 보강
enablePassengerPortalFix = true
# 차원별 마지막 포탈 좌표를 기억해 되돌아올 때 같은 포탈로 배정
enablePortalRedirect = true
```

#### Safety Guard (dontDoThat 스타일)
```toml
[general.safety_guard]
# 길들여진 동물, 주민, 장식물을 실수로 공격하는 것을 막습니다
enableActionGuard = true

# 길들여진 애완동물을 보호합니다
protectTamedPets = true

# 주민/행상인을 보호합니다
protectVillagers = true

# 아이템 액자/그림 등 장식 엔티티를 보호합니다 (액자 안이 비어 있으면 자동 허용)
protectDecorations = true

# 웅크린 상태(Shift)에서는 보호를 우회할 수 있도록 허용합니다
allowSneakBypass = true
```

#### Buckets
```toml
[general.buckets]
# 버킷 안의 액체나 엔티티 정보를 툴팁으로 보여줍니다 (클라이언트 전용)
enableBucketPreview = true
```

#### Interaction Fallthrough (RCF 스타일)
```toml
[gameplay.interaction_fallthrough]
# 오른손 아이템 사용이 실패했을 때 자동으로 왼손 아이템 사용을 시도합니다
# 예: 오른손에 블록이 있지만 설치할 공간이 없을 때 왼손의 블록을 사용
enableRightClickFallthrough = true
```

#### Item Frame Silence (BugFixerUpper 스타일)
```toml
[gameplay.item_frame_silence]
# 청크 로딩/월드 생성 시 아이템 액자가 로드될 때 재생되는 불필요한 소리를 음소거합니다
# 플레이어가 직접 아이템 액자를 설치할 때는 정상적으로 소리가 재생됩니다
enableItemFrameSilence = true
```

#### Swim State Fix (Swim Fix 스타일)
```toml
[gameplay.swim_state_fix]
# 물 속에서 수영 중 공격 시 발생하는 수영 상태 동기화 버그를 수정합니다 (MC-220390)
# 수영 중 엔티티를 공격하면 서버가 수영 상태를 잘못 해제하여 히트박스 불일치가 발생하는 문제를 해결합니다
# 해저 신전(Ocean Monument)에서 가디언과 싸울 때 특히 유용합니다
enableSwimStateFix = true
```

#### Enchanting (Enchanter Fix 스타일)
```toml
[general.enchanting]
# 테이블 슬롯이 변할 때마다 마법 부여 시드를 새로 뽑고 즉시 클라이언트에 동기화합니다
# Enchanter Fix 모드와 유사한 기능이지만 MCOPT 자체 구현입니다
# - 시드가 변경될 때마다 broadcastChanges()를 호출하여 클라이언트 동기화 보장
# - 바닐라의 "오래된 마법 부여 옵션 표시" 버그를 수정
# - 플레이어 고정 시드 대신 진정한 무작위 시드를 사용하여 예측을 어렵게 만듭니다
fixEnchantmentRNG = true
```

#### Memory Management
```toml
[general.memory]
# Enable memory management optimizations
enableMemoryOptimizations = true
# Prevent garbage collection during rendering
aggressiveGCPrevention = true
# Enable object pooling for Vec3 and BlockPos (reduces GC pressure)
enableObjectPooling = true
# Enable aggressive resource cleanup on world unload/disconnect
enableResourceCleanup = true
# Show memory usage HUD in top-left corner
showMemoryHud = true
# Enable AllTheLeaks 스타일 누수 감시
enableLeakGuard = true
# 실행 중인 스레드를 방해하지 않고 유휴 상태에서만 청소
leakSafeCleanup = true
# 월드 언로드 후 경고를 띄우기까지 대기할 틱 수 (기본: 200틱 = 10초)
leakCheckDelayTicks = 200
# 경고를 발생시킬 메모리 사용량(MB)
leakMemoryAlertMb = 4096
# 누수가 의심될 때 단 한 번 GC를 시도
leakGcNudge = false
# 추가 경고를 출력하기 전 대기할 틱 수 (로그 스팸 방지)
leakWarningIntervalTicks = 200
# 메모리 경고 간 최소 쿨다운(초)
leakMemoryAlertCooldownSeconds = 15
```

#### Max Health 안정화
```toml
[general.health]
# MAX_HEALTH가 변할 때 현재 체력 비율을 그대로 유지
enableMaxHealthStability = true
```

#### 속성 한도 확장
```toml
[general.attributes]
# 바닐라 1024 상한 대신 더 높은 값으로 클램프 (AttributeFix 스타일)
enableAttributeRangeExpansion = true
# 클램프 상한 (기본: 1,000,000,000)
attributeMaxLimit = 1000000000
```

#### 포션 레벨 제한 해제
```toml
[safety.potion_fix]
# 포션 효과 레벨(Amplifier)이 byte 범위(127)를 초과해도 정상 작동하도록 수정
# - 바닐라는 내부적으로 int를 사용하지만 NBT 저장 시 byte로 변환되어 128+ 레벨에서 오버플로우 발생
# - 예: Haste 128레벨이 역효과를 내거나, Levitation이 중력을 증가시키는 버그 방지
# - 모드로 극한 포션 효과를 사용하는 경우 활성화 권장
enablePotionLimitFix = true
```

#### 즉시 기상 시간 동기화
```toml
[safety.instant_wakeup]
# 플레이어가 침대에서 일어날 때 시간 변경을 즉시 클라이언트에 동기화합니다
# Instantly syncs time to clients when all players wake up from sleep
# 바닐라는 20틱마다 시간을 전송하므로 최대 1초 딜레이 발생
enableInstantWakeup = true
```

#### Allay 영속성 보장
```toml
[safety.allay_fix]
# Allay가 아이템을 들고 있을 때 디스폰되지 않도록 방지합니다
# Prevents Allay from despawning when holding items
# 아이템 수집 중인 Allay가 사라지는 문제를 해결합니다
enableAllayPersistenceFix = true
```

#### 조용한 번개
```toml
[safety.silent_lightning]
# 번개가 Silent 태그를 가지고 있을 때 소리를 재생하지 않습니다
# Prevents lightning from playing sound when it has the Silent tag
# 명령어로 조용한 번개를 소환할 때 유용합니다
enableSilentLightningFix = true
```

#### 청크 당 엔티티 제한
```toml
[safety.per_chunk_entity_limit]
# 특정 청크에 엔티티가 과도하게 밀집되는 것을 방지합니다
enablePerChunkEntityLimit = false
# 청크 당 최대 엔티티 수 (초과 시 오래된 엔티티부터 제거)
maxEntitiesPerChunk = 50
# 몬스터를 제한 대상에 포함
limitMonsters = true
# 동물을 제한 대상에 포함
limitAnimals = true
# 아이템 엔티티를 제한 대상에 포함
limitItems = true
# 청크가 꽉 찼을 때 새로운 스폰을 막습니다 (제거 대신 예방)
preventSpawnWhenFull = false
```

#### 로그인 타임아웃 방지
```toml
[gameplay.login_timeout]
# 무거운 모드팩에서 로그인 타임아웃을 방지합니다
enableLoginTimeoutFix = true
# 로그인 핸드셰이크 타임아웃 시간 (초)
# 바닐라 기본값: 30초
# 권장값: 120-180초 (무거운 모드팩의 경우)
loginTimeoutSeconds = 120
```

#### 월드 생성 안정화
```toml
[gameplay.world_generation]
# 커스텀 지형 생성 중 호수 기능으로 인한 크래시를 방지합니다
# 로드되지 않은 청크의 바이옴을 확인할 때 발생하는 오류를 안전하게 처리합니다
enableLakeCrashFix = true

# 오버월드 바이옴에 돌 디스크(Basin) 피처를 복원합니다
# Plains, Forest, Savanna 등의 바이옴에서 자연스러운 돌 지형 생성을 활성화합니다
enableBasinFix = true
```

#### Experience Orb Merging
```toml
[general.xp_orb_merging]
# Enable experience orb merging
enableXpOrbMerging = true
# Clumps 모드가 설치되어 있으면 자동으로 비활성화됩니다
# Merge radius in blocks (0.5-5.0, default: 1.5)
# Larger radius = more aggressive merging
xpOrbMergeRadius = 1.5
# Merge check delay in ticks (1-40, default: 10)
# Lower = more frequent merging, higher = less CPU usage
xpOrbMergeDelay = 10
```

#### Clear Lag 스케줄러
```toml
[general.clear_lag]
# 주기적으로 지상에 남은 엔티티(아이템/경험치/투사체)를 정리합니다
enableClearLag = false

# 정리 주기 (틱). 20틱 = 1초, 기본값 6000틱(5분)
clearLagIntervalTicks = 6000

# 정리 직전 경고를 보낼 시점 (틱). 0이면 경고를 생략합니다
clearLagWarningTicks = 200

# 제거 범위 설정
clearLagRemoveItems = true
clearLagRemoveXpOrbs = true
clearLagRemoveProjectiles = true

# 이름표가 붙은 아이템은 보호합니다
clearLagSkipNamedItems = true

# 제거 대상에서 제외할 엔티티 ID 리스트
clearLagEntityWhitelist = ["minecraft:armor_stand"]
```

#### 포션 스택 시스템
```toml
[gameplay.potion_stacking]
# 물병과 포션을 겹칠 수 있게 합니다
# Allows potions and bottles to stack
enablePotionStacking = true

# 포션/물병의 최대 스택 크기 (1-64)
# Maximum stack size for potions and bottles
potionStackSize = 16
```

> [!NOTE]
> 양조기 사용 시: 스택된 포션을 1개씩 분리하여 넣는 것을 권장합니다.

#### Entity AI Optimization
```toml
[general.ai_optimizations]
# Enable AI optimization system
enableAiOptimizations = true
# AI-Improvements가 설치되어 있으면 자동으로 비활성화됩니다
# Enable math function caching (atan2, sin, cos)
enableMathCache = true
# Replace mob LookControl with optimized version
enableOptimizedLookControl = true

[general.ai_optimizations.common_goals]
# Remove LookAtPlayerGoal from all mobs
removeLookAtPlayer = false
# Remove RandomLookAroundGoal from all mobs
removeRandomLookAround = false

[general.ai_optimizations.animal_goals]
# Animal AI goal removal (applies to Cow, Pig, Chicken, Sheep)
removeFloat = false        # WARNING: Animals may not swim!
removePanic = false        # Animals won't flee when attacked
removeBreed = false        # Disables breeding
removeTempt = false        # Won't follow food
removeFollowParent = false # Babies won't follow parents
removeStroll = false       # Major performance gain, makes animals static

[general.ai_optimizations.sheep_goals]
# Sheep-specific AI goal removal
removeEatBlock = false     # Sheep won't eat grass to regrow wool

[general.ai_optimizations.aquatic_goals]
# Aquatic mob AI goal removal
removeFishSwim = false         # Fish won't swim randomly
removeFishPanic = false        # Fish won't flee
removeSquidRandomMovement = false  # Major ocean performance gain
removeSquidFlee = false        # Squids won't flee from players
```

## Performance Tips

For best performance in singleplayer:
1. Enable all optimizations in the config
2. Enable `enableEllipticalRenderDistance` for 10-35% FPS boost
3. Enable `enableSmartLeaves` for 10-40% FPS boost in forest biomes
4. Enable `enableXpOrbMerging` to reduce lag during mob farming/mining
5. Enable `enableObjectPooling` and `showMemoryHud` to monitor and reduce GC pressure
6. Enable `enableAiOptimizations` for better performance with many mobs
7. Use **F8 (Panic Button)** when experiencing sudden lag to free memory
8. Set `chunkUpdateLimit` to 4-6 for smooth FPS
9. Set `verticalRenderStretch` to 0.5-0.75 for better performance
10. Set `entityCullingDistance` based on your render distance (32-64 for normal, 64-128 for high)
11. Set `particleSpawnReduction` to 0.25-0.5 depending on your preferences
12. For mob farms: Enable `removeStroll`, `removeRandomLookAround` for major performance gains
13. For dense forests: Set `leavesCullingDepth` to 0 for maximum performance

For high-end systems:
- Increase `chunkUpdateLimit` to 10-15 for faster world updates
- Set `verticalRenderStretch` to 1.0-1.5 to see more vertically
- Set `horizontalRenderStretch` to 1.2-1.5 to extend horizontal view
- Increase `maxParticlesPerFrame` to 1000-2000 for more particles
- Set `xpOrbMergeRadius` to 2.0-3.0 for more aggressive merging
- Set `xpOrbMergeDelay` to 5-10 for more frequent checks
- Disable `aggressiveChunkCulling` if you notice pop-in

For low-end systems:
- Decrease `chunkUpdateLimit` to 2-4
- Set `verticalRenderStretch` to 0.3-0.5 for maximum FPS
- Set `horizontalRenderStretch` to 0.8-0.9 to reduce chunk load
- Set `entityCullingDistance` to 32-48
- Increase `particleSpawnReduction` to 0.5-0.75
- Set `xpOrbMergeRadius` to 2.5-5.0 for maximum orb reduction
- Set `xpOrbMergeDelay` to 15-20 to reduce CPU overhead
- Enable `aggressiveChunkCulling`
- **Smart Leaves for low-end**: Set `leavesCullingDepth` to 0 for maximum forest performance
- **AI Optimizations for low-end**: Enable `removeStroll`, `removeRandomLookAround`, `removeBreed` for passive mobs
- For ocean biomes: Enable `removeSquidRandomMovement` and `removeFishSwim` for major gains

## Key Bindings

MCOPT adds the following key bindings (configurable in Minecraft's Controls menu):

| Key | Function | Description |
|-----|----------|-------------|
| **F8** | Memory Panic Button | Triggers emergency memory cleanup (GC + pool clearing) with 5-second cooldown |

The panic button provides instant feedback via chat message showing how much memory was freed.

## Commands

MCOPT provides diagnostic commands to help monitor and troubleshoot performance issues:

### `/mcopt status` (or `/mcopt report`)

Displays a comprehensive status report of MCOPT's current state:

**Memory Usage:**
- Current RAM usage and percentage
- Color-coded warnings (green < 75%, yellow < 90%, red ≥ 90%)

**Active Modules:**
- Lists all enabled optimization features
- Shows configuration values for key settings
- Displays which features are currently running

**Performance Statistics:**
- Entity culling distance
- Block entity culling distance
- Per-chunk entity limits
- Memory optimization status

**Detected Conflicts:**
- Identifies incompatible mods that are installed
- Shows which MCOPT features were auto-disabled
- Helps diagnose mod conflicts (e.g., Clumps, Dynamic FPS, AI Improvements)

**Example Output:**
```
═══════════════════════════════════
           MCOPT Status Report
═══════════════════════════════════

Memory Usage:
  Used: 2048MB / 4096MB (50%)

Active Modules:
  Chunk Optimizations: ON
  Entity Culling: ON
  Block Entity Culling: ON
  Particle Optimizations: ON
  AI Optimizations: ON
  Dynamic FPS: ON
  Per-Chunk Entity Limit: OFF

Configuration:
  Entity Culling Distance: 64 blocks
  Block Entity Culling Distance: 64 blocks
  Memory Optimizations: Active

No Conflicts Detected
═══════════════════════════════════
```

**Permissions:** This command is available to all players in singleplayer and to operators on servers.

## Compatibility

MCOPT is designed with mod compatibility as the highest priority:
- Uses non-invasive Mixin injections
- Preserves vanilla behavior and API contracts
- Compatible with most other performance mods
- Safe to use with content mods (no gameplay changes)

### Known Compatible Mods
- **FerriteCore**: Perfect compatibility - MCOPT handles dynamic memory while FerriteCore handles static memory
- Shader mods (OptiFine alternatives)
- World generation mods
- Content and gameplay mods
- Other performance mods (test before using together)

### Potential Conflicts
If you experience issues with other mods, try:
1. Disabling specific optimizations in the config
2. Reporting the issue on our [GitHub Issues](https://github.com/randomstrangerpassenger/MCOPT/issues) page

### 자동 호환성 보호
- Clumps 설치 시: 경험치 병합 최적화 자동 비활성화
- AI-Improvements 설치 시: 엔티티 AI 최적화 자동 비활성화
- Dynamic FPS/FPS Reducer 설치 시: 내장 동적 FPS 컨트롤러 자동 비활성화
- AllTheLeaks/MemoryLeakFix 설치 시: Leak Guard 자동 비활성화
- cull-leaves/moreculling/optileaves/cull-less-leaves 설치 시: Smart Leaves Culling 자동 비활성화

## Building from Source

### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Git

### Build Steps
```bash
git clone https://github.com/randomstrangerpassenger/MCOPT.git
cd MCOPT
./gradlew build
```

The compiled mod will be located in `build/libs/mcopt-1.0.0.jar`

### Development Setup
```bash
# For IntelliJ IDEA
./gradlew genIntellijRuns

# For Eclipse
./gradlew eclipse

# For VSCode
./gradlew genVSCodeRuns
```

### Running Benchmarks

MCOPT includes JMH (Java Microbenchmark Harness) benchmarks to verify the effectiveness of optimizations like MathCache on different Java versions and CPU architectures.

```bash
# Run all benchmarks
./gradlew jmh -PenableJmh

# Run specific benchmark
./gradlew jmh -PenableJmh -Pjmh.includes='MathCacheBenchmark'
```

**Interpreting Benchmark Results:**

For `MathCacheBenchmark`:
- Compare `baselineAtan2_JavaMath` vs `optimizedAtan2_MathCache`
- Compare `baselineSin_JavaMath` vs `optimizedSin_MathCache`
- Check `realWorldScenario_*` benchmarks for actual AI usage patterns

**Decision Criteria:**
- If MathCache.atan2 is > 50% faster: Keep atan2 caching ✅
- If MathCache.sin/cos is < 20% faster: Consider removing (Java 21+ has fast native impl) ⚠️
- Check memory footprint: sin/cos tables use 32KB combined 💾

**Note:** Modern CPUs (2020+) with Java 21+ may show minimal benefit from sin/cos caching due to hardware-accelerated Math functions. The atan2 cache typically remains beneficial across all platforms.

## Technical Details

### Architecture
- **Platform**: NeoForge 21.1.77+
- **Minecraft Version**: 1.21.1
- **Language**: Java 21
- **Injection Method**: Mixin

### Code Quality & Maintainability
MCOPT follows modern Java best practices with a focus on maintainability:

#### Documentation
- **87% JavaDoc Coverage**: All public APIs fully documented with purpose, parameters, and return values
- **Package Documentation**: Every package has comprehensive `package-info.java` explaining its role and design
- **Inline Comments**: Complex algorithms and performance optimizations include detailed explanations
- **Architectural Notes**: Class-level documentation explains design decisions and trade-offs

#### Code Organization
- **Centralized Constants**: All magic numbers extracted to `MCOPTConstants` utility class
  - Minecraft constants (chunk sizes, offsets)
  - UI constants (colors, margins, update intervals)
  - Performance constants (thresholds, timeouts)
- **Clean Package Structure**:
  - `ai` - AI optimization system with filters and modifiers
  - `client` - Client-side rendering and memory tools
  - `config` - 65+ configuration options with validation
  - `mixin` - Non-invasive Mixin injections
  - `util` - Shared utilities and constants
- **Zero Dead Code**: Unused legacy code removed for clarity
- **Consistent Naming**: All Mixin members use `mcopt$` prefix to avoid conflicts

#### Logging & Debugging
- **Structured Logging**: SLF4J with appropriate log levels (INFO, DEBUG, WARN, ERROR)
- **Performance Metrics**: Initialization times, memory usage, and optimization effects logged
- **Context-Aware Messages**: All logs include relevant entity types, counts, or error details
- **No Sensitive Data**: Logs contain only technical information safe for sharing

### Optimization Techniques
1. **Frame-based throttling**: Prevents overwhelming the render thread
2. **Spatial culling**: Skips rendering objects outside the view frustum
3. **Elliptical render distance**: Renders chunks in 3D ellipsoid instead of square/cylinder
4. **Distance-based LOD**: Reduces detail for distant objects
5. **Probabilistic reduction**: Maintains visual quality while reducing load
6. **Calculation caching**: Avoids redundant expensive operations
7. **Configurable stretch factors**: Fine-tune vertical/horizontal render shapes
8. **Entity merging**: Combines nearby experience orbs to reduce entity count
9. **Object pooling**: Reuses Vec3 and BlockPos instances to prevent GC spikes
10. **Smart resource cleanup**: Aggressive cleanup on world unload/disconnect
11. **Memory monitoring**: Real-time HUD and emergency cleanup button
12. **Math caching**: Pre-computed trigonometric lookup tables for AI calculations
13. **AI goal filtering**: Selective removal of expensive AI behaviors
14. **Optimized mob controllers**: Replacement of vanilla LookControl with cached version

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

### Guidelines

#### Code Quality
- **Follow existing code style**: Use consistent naming, indentation, and organization
- **No magic numbers**: Extract constants to `MCOPTConstants` or create new constant classes
- **Add JavaDoc**: All public methods, classes, and interfaces must have JavaDoc
- **Update package-info.java**: If adding new packages, include comprehensive package documentation
- **Remove dead code**: Don't leave commented-out code or unused imports
- **Use Mixin prefixes**: All Mixin members must use `mcopt$` prefix

#### Testing & Compatibility
- **Test thoroughly**: Verify changes in singleplayer and multiplayer environments
- **Ensure mod compatibility**: Test with popular mods (FerriteCore, shaders, etc.)
- **Check performance impact**: Measure FPS, memory usage, and startup time
- **Preserve vanilla behavior**: Don't break core game mechanics or APIs

#### Documentation
- **Document new features**: Update README.md with feature descriptions and config options
- **Add configuration examples**: Include `.toml` examples for new config options
- **Update performance tips**: If applicable, add recommendations for new features
- **Write clear commit messages**: Explain what changed and why

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Inspired by optimization strategies and bug fixes from:
  - **Sodium, Lithium, Embeddium**: Rendering and logic optimizations
  - **AI-Improvements**: Entity AI optimization concepts
  - **FerriteCore**: Memory optimization approaches
  - **Enchanter Fix**: Enchantment seed synchronization fix approach
  - **NeoBeeFix**: Bee pathfinding stability improvements
  - **AllTheLeaks**: Memory leak detection patterns
  - **AttributeFix**: Attribute range expansion concepts
  - **PotionLevelFix**: Potion amplifier byte overflow fix concepts
  - **dontDoThat**: Safety guard inspiration
  - **Simple Snowy Fix**: Snow accumulation optimization
  - **OptiLeaves**: Smart leaves culling techniques
  - **Clumps**: Experience orb merging inspiration
  - **Dynamic FPS / FPS Reducer**: FPS throttling concepts
  - **Fishing Rod Fix**: Fishing bobber cleanup patterns
  - **Redirected**: Portal redirect mechanism inspiration
  - **Lake Feature Fix**: Lake generation crash prevention patterns
  - **Basin Generation Fix**: Basin (stone disk) feature restoration
  - **RCF (Right Click Fallthrough)**: Interaction fallthrough mechanism inspiration
  - **BugFixerUpper**: Item frame silence fix inspiration
  - **Swim Fix**: Swimming state desync fix inspiration (MC-220390)
- All implementations are original and independent
- Thanks to the NeoForge team for the excellent modding platform

## Support

- **Issues**: [GitHub Issues](https://github.com/randomstrangerpassenger/MCOPT/issues)
- **Discussions**: [GitHub Discussions](https://github.com/randomstrangerpassenger/MCOPT/discussions)

---

**Note**: This mod is focused on client-side performance improvements. While it works on servers, the primary benefits are seen in singleplayer or as a client-side mod in multiplayer.
