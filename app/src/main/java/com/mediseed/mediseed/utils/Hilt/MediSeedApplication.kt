package com.mediseed.mediseed.utils.Hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**MediSeed의 의존성 주입(DI)은 단방향으로 이루어집니다.**/
/**MediSeedApplication에서 Hilt의 Entry Point 및 Root Container가 생성됩니다.**/
/**NetworkModule: Retrofit -> RepositoryImpl │ BindModule: UseCase -> ViewModel -> View**/
/**UseCase를 ViewModel로 곧바로 주입했기 때문에, ViewModelFactory는 사용하지 않습니다.**/
@HiltAndroidApp
class MediSeedApplication : Application()