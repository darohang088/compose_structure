# Clean Architecture: The Use Case Layer

## What is a Use Case?
In Clean Architecture, a **Use Case** (also known as an Interactor) sits in the **Domain Layer**. It represents a single, specific business action or feature of your application. Examples include `LoginUserUseCase`, `GetUserProfileUseCase`, or `ValidateBiometricSignatureUseCase`.

A Use Case is the "conductor" of the orchestra. It doesn't know *how* data is fetched from the internet, and it doesn't know *how* data is displayed on the screen. It only knows the business rules required to get the job done.

---

## The Execution Flow

In a properly structured Clean Architecture application, the flow of data is strictly one-way:

**UI (Compose) ➔ ViewModel ➔ Use Case ➔ Repository ➔ Data Source (API / DB)**

### 1. The Presentation Layer (UI & ViewModel)
The UI registers a user action (e.g., clicking a button). The `ViewModel` receives this action. Instead of the ViewModel talking directly to a database or network, it asks the Use Case to do the work.
```kotlin
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase // Inject UseCase, NOT Repository
) : ViewModel() {
    
    fun fetchProfile() {
        viewModelScope.launch {
            // The ViewModel simply calls the Use Case and waits for the result
            getUserProfileUseCase().collect { state ->
                _uiState.value = state
            }
        }
    }
}
```

### 2. The Domain Layer (Use Case)
The Use Case contains the pure business logic. It usually injects a Repository Interface. When the ViewModel calls the Use Case, the Use Case executes its logic (e.g. validating input) and then calls the Repository to get or save data.
```kotlin
class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    // Overriding the invoke() operator lets us call the class like a function
    suspend operator fun invoke(): Flow<UiState<ProfileEntity>> {
        // Business logic can go here (e.g., checking if the user is authorized)
        return repository.fetchProfile()
    }
}
```

### 3. The Data Layer (Repository & Data Source)
The Data Layer implements the Repository Interface. It talks to the actual data sources (Retrofit API or Room Database), fetches Data Transfer Objects (DTOs), maps them to clean Kotlin `Entities`, and returns them back up the chain.

---

## Why Use Use Cases instead of calling the Repository directly?

1. **Single Responsibility Principle:** A `UserRepository` might have 20 methods (`login()`, `logout()`, `updateProfile()`, `deleteAccount()`). If your ViewModel injects the Repository, it has access to all 20 methods, even if it only needs one. By injecting a `LoginUserUseCase`, the ViewModel is strictly limited to doing exactly what it's supposed to do.
2. **Reusability:** Business logic often needs to be reused across different ViewModels. If you put logic (like formatting data or chaining multiple API calls together) inside a ViewModel, you can't easily reuse it. Putting it in a Use Case makes it globally available.
3. **Decoupling:** The Use Case belongs to the Domain layer, which contains zero Android dependencies (No `Context`, no `Retrofit`). This makes your business logic pure and easily testable.
4. **Easier Unit Testing:** Because a Use Case does exactly one thing, writing unit tests for it is incredibly straightforward. You mock the Repository, call the Use Case, and assert the output.
