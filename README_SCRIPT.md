# Auto-generate Android Features

This directory includes a bash script `new_feature.sh` that makes it dramatically easier and faster to scaffold entire feature domains within the Jetpack Compose MVVM Android application.

## Prerequisites

Before running the script, make sure it has the correct execute permissions. Open your terminal at the root of the project and run:

```bash
chmod +x new_feature.sh
```

## Usage

When you're ready to create a new generic feature (for example, `home` or `profile`), run:

```bash
./new_feature.sh home
```

### Script Behavior
- The script takes a singular, **lowercase** string representing the domain of the feature.
- Behind the scenes, it converts your input to PascalCase (e.g. `Home`).
- It completely scaffolds your MVVM + Clean Architecture flow, generating:
  - `${PASCAL_NAME}Model.kt`
  - `${PASCAL_NAME}ApiService.kt`
  - `${PASCAL_NAME}Repository.kt`
  - `${PASCAL_NAME}Module.kt`
  - `${PASCAL_NAME}ViewModel.kt`
  - `${PASCAL_NAME}Screen.kt`
- If you've already generated a feature using the identical name, it safely **aborts** without overwriting your existing code.

## Modifying the Flow
The script outputs standard dummy endpoints and generic text schemas (i.e. `id`, `name`, `description`).

**To change fields:**
After generating the script, immediately open up the auto-generated `.../data/model/YourModel.kt` file and adjust the `@SerializedName` objects directly before you start consuming them within your `YourScreen.kt`. Example:
```kotlin
data class ProfileModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name") // Adjusted field
    val firstName: String,
    @SerializedName("email") // Adjusted field
    val email: String
)
```

## Example Log Output

```text
$ ./new_feature.sh home
Generating feature: home (Class prefix: Home)

✅ Successfully generated feature: home
The following files were created in app/src/main/java/com/example/app/feature/home:
  - data/model/HomeModel.kt
  - data/remote/HomeApiService.kt
  - data/repository/HomeRepository.kt
  - di/HomeModule.kt
  - viewmodel/HomeViewModel.kt
  - ui/HomeScreen.kt

Don't forget to run Gradle Sync if your IDE doesn't pick them up automatically!
```
