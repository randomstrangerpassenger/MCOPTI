# Event Bus Registration Guide

## Overview
MCOPT에서 이벤트 핸들러를 등록하는 표준 방법을 정의합니다.

NeoForge는 두 가지 이벤트 버스를 제공합니다:
- **NeoForge Event Bus**: 게임 이벤트 (entity, level, tick, etc.)
- **Mod Event Bus**: Mod lifecycle 이벤트 (setup, config, etc.)

---

## 등록 패턴 규칙

### Rule 1: Mod Lifecycle 이벤트 → `modEventBus.addListener()`

**사용 시기**:
- Mod lifecycle 이벤트 처리
- Setup 단계에서 초기화 필요

**이벤트 타입**:
- `FMLCommonSetupEvent`
- `FMLClientSetupEvent`
- `ModConfigEvent.Reloading`
- 기타 Mod 버스 이벤트

**위치**: `MCOPT.java` 생성자

**예시**:
```java
public MCOPT(IEventBus modEventBus, ModContainer modContainer) {
    // Setup listeners
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::clientSetup);
    
    // Config reload listener
    modEventBus.addListener(FeatureToggles::onModConfigReloaded);
}

private void commonSetup(final FMLCommonSetupEvent event) {
    // Common initialization
}

private void clientSetup(final FMLClientSetupEvent event) {
    // Client-side initialization
}
```

---

### Rule 2: 무조건 활성화 + Static 메서드 → `@EventBusSubscriber`

**사용 시기**:
- 항상 활성화되는 핸들러
- Static 메서드만 사용
- Feature toggle 없음
- Instance 상태 불필요

**장점**:
- 자동 등록 (수동 코드 불필요)
- 명확한 의도 표현
- 메모리 효율적 (인스턴스 생성 X)

**Common 핸들러**:
```java
@EventBusSubscriber(modid = MCOPT.MOD_ID)
public final class ConfigCacheManager {
    
    private ConfigCacheManager() {
        // Utility class
    }
    
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (!event.getConfig().getModId().equals(MCOPT.MOD_ID)) {
            return;
        }
        // Refresh config caches
    }
}
```

**Client 전용 핸들러**:
```java
@EventBusSubscriber(modid = MCOPT.MOD_ID, value = Dist.CLIENT)
public final class LeakGuard {
    
    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (!event.getLevel().isClientSide()) {
            return;
        }
        // Client-side cleanup
    }
}
```

---

### Rule 3: 조건부 활성화 OR Instance 상태 → 수동 등록

**사용 시기**:
- Feature toggle 기반 조건부 활성화
- Runtime 상태 유지 필요 (instance 필드)
- 동적으로 활성화/비활성화

**위치**: `MCOPT.java` (적절한 setup 메서드)

**Feature Toggle 예시**:
```java
// In MCOPT.java constructor
if (FeatureToggles.isEnabled(FeatureKey.ACTION_GUARD)) {
    NeoForge.EVENT_BUS.register(new ActionGuardHandler());
}
```

**Instance 상태 예시**:
```java
// In MCOPT.java clientSetup
if (FeatureToggles.isEnabled(FeatureKey.DYNAMIC_FPS)) {
    NeoForge.EVENT_BUS.register(new DynamicFpsManager());
}

// DynamicFpsManager.java
public class DynamicFpsManager {
    private int userDefinedFramerate = -1;  // Instance state
    private long lastCheckTime = 0;
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        // Use instance fields
    }
}
```

---

## 결정 흐름도

```mermaid
graph TD
    A[이벤트 핸들러 필요] --> B{Mod lifecycle 이벤트?}
    B -->|Yes| C[modEventBus.addListener<br/>MCOPT.java]
    B -->|No| D{조건부 활성화 필요?}
    D -->|Yes| E[수동 등록<br/>NeoForge.EVENT_BUS.register]
    D -->|No| F{Instance 상태 필요?}
    F -->|Yes| E
    F -->|No| G{Client 전용?}
    G -->|Yes| H[@EventBusSubscriber<br/>value = Dist.CLIENT]
    G -->|No| I[@EventBusSubscriber<br/>static methods only]
```

---

## 현재 MCOPT 핸들러

### @EventBusSubscriber (6개)
| 핸들러 | Dist | 설명 |
|--------|------|------|
| ConfigCacheManager | Both | Config cache 갱신 |
| HealthStabilityHandler | Both | 체력 안정성 |
| PortalRedirectionHandler | Both | 포털 리다이렉션 |
| FishingRodFixHandler | Both | 낚싯대 수정 |
| LeakGuard | CLIENT | 메모리 누수 감지 |
| ResourceCleanupHandler | CLIENT | 리소스 정리 |

### 수동 등록 (2개)
| 핸들러 | Toggle | 위치 | 이유 |
|--------|--------|------|------|
| ActionGuardHandler | ACTION_GUARD | MCOPT.java | 조건부 |
| DynamicFpsManager | DYNAMIC_FPS | MCOPT.java | 조건부 + Instance |

### modEventBus (3개)
| 리스너 | 이벤트 | 위치 |
|--------|--------|------|
| FeatureToggles::onModConfigReloaded | ModConfigEvent.Reloading | MCOPT.java |
| this::commonSetup | FMLCommonSetupEvent | MCOPT.java |
| this::clientSetup | FMLClientSetupEvent | MCOPT.java |

---

## 일반적인 실수

### ❌ Static 핸들러를 수동 등록
```java
// BAD: Static 메서드만 사용하는데 수동 등록
NeoForge.EVENT_BUS.register(new ConfigCacheManager());
```
```java
// GOOD: @EventBusSubscriber 사용
@EventBusSubscriber(modid = MCOPT.MOD_ID)
public final class ConfigCacheManager {
    @SubscribeEvent
    public static void onConfigReload(...) { }
}
```

### ❌ Instance 필드를 @EventBusSubscriber에서 사용
```java
// BAD: Static 클래스에서 instance 필드
@EventBusSubscriber(modid = MCOPT.MOD_ID)
public class BadHandler {
    private int counter = 0;  // ❌ Won't work with static!
    
    @SubscribeEvent
    public static void onEvent(...) {
        counter++;  // ❌ Compile error
    }
}
```
```java
// GOOD: 수동 등록 + instance
public class GoodHandler {
    private int counter = 0;  // ✅ Instance field
    
    @SubscribeEvent
    public void onEvent(...) {
        counter++;  // ✅ Works
    }
}

// Register in MCOPT.java
NeoForge.EVENT_BUS.register(new GoodHandler());
```

### ❌ 조건부 핸들러를 @EventBusSubscriber로
```java
// BAD: Toggle 체크를 핸들러 내부에서
@EventBusSubscriber(modid = MCOPT.MOD_ID)
public class ConditionalHandler {
    @SubscribeEvent
    public static void onEvent(...) {
        if (!FeatureToggles.isEnabled(FeatureKey.SOME_FEATURE)) {
            return;  // ❌ Handler still registered, wastes CPU
        }
        // ...
    }
}
```
```java
// GOOD: 등록 자체를 조건부로
// In MCOPT.java
if (FeatureToggles.isEnabled(FeatureKey.SOME_FEATURE)) {
    NeoForge.EVENT_BUS.register(new ConditionalHandler());
}
```

---

## 체크리스트

새 이벤트 핸들러 추가 시:

- [ ] Mod lifecycle 이벤트인가? → `modEventBus.addListener()`
- [ ] Feature toggle이 있는가? → 수동 등록
- [ ] Instance 필드가 필요한가? → 수동 등록
- [ ] 항상 활성화 + Static인가? → `@EventBusSubscriber`
- [ ] Client 전용인가? → `value = Dist.CLIENT` 추가
- [ ] `MCOPT.MOD_ID` 사용했는가?

---

## 참고

- [NeoForge Event Bus Documentation](https://docs.neoforged.net/docs/events/)
- [FeatureToggles.java](file:///c:/Users/MW/Downloads/coding/MCOPTI/src/main/java/com/randomstrangerpassenger/mcopt/util/FeatureToggles.java)
- [MCOPT.java](file:///c:/Users/MW/Downloads/coding/MCOPTI/src/main/java/com/randomstrangerpassenger/mcopt/MCOPT.java)
