# Spring Boot 4.0.0 Upgrade Notes

## Completed Changes

This project has been successfully upgraded from Spring Boot 3.5.0 to Spring Boot 4.0.0 (released November 20, 2025).

### Main Project Changes
- ✅ Updated `build.gradle.kts` to Spring Boot 4.0.0
- ✅ Fixed Spring Security API changes for nullable Authentication
- ✅ Translated all Russian comments to English
- ✅ Build verified successful

### Submodule Changes Required

The `spring-module-tools` submodule also requires updates for Spring Boot 4 compatibility. The following changes have been made locally but need to be committed to the submodule repository (https://github.com/ViRGiL175/spring-module-tools.git):

1. **build.gradle.kts**: Updated Spring Boot version from 3.5.0 to 4.0.0
2. **Removed**: `src/main/kotlin/ru/virgil/spring/tools/date/JsonDateFormatConfig.kt` (no longer needed with Spring Boot 4)
3. **Security.kt**: 
   - Fixed `getAuthentication()` return type to nullable `Authentication?`
   - Updated `getPrincipal()` to handle nullable authentication
   - Translated Russian comment to English
4. **JpaUserDetailsManager.kt**:
   - Fixed `loadUserByUsername()` to throw `UsernameNotFoundException` instead of returning null
   - Fixed `changePassword()` to handle nullable Authentication and nullable encoded password
   - Fixed `initDefaultUser()` to use non-null assertion for username

### Next Steps for Submodule

To complete the upgrade, the submodule maintainer should:
1. Check out the `spring-module-tools` repository
2. Apply the same changes listed above
3. Test and commit to the submodule repository
4. Update the submodule reference in this repository

Alternatively, you can commit the current submodule changes directly if you have permissions to the submodule repository.

## Spring Boot 4 API Changes Summary

### Security Changes
- `SecurityContextHolder.getContext().getAuthentication()` now returns nullable `Authentication?`
- `UserDetailsService.loadUserByUsername()` must never return null (throw `UsernameNotFoundException` instead)
- `PasswordEncoder.encode()` can return nullable `String?`

### Jackson Changes
- Spring Boot 4 uses Jackson 3 by default
- JSR310 date/time types are automatically handled with ISO-8601 format
- Custom date format configuration is usually unnecessary

## Build Status
- ✅ Main project builds successfully with Spring Boot 4.0.0
- ✅ All tests compile (test execution requires updated submodule)
- ✅ Security scan: No vulnerabilities detected
- ✅ Code review: No issues found
