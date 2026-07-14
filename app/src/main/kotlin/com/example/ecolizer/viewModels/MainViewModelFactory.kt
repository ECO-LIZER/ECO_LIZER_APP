package com.example.ecolizer.viewModels 

class MainViewModelFactory(
    private val falloDao: FalloDao,
    private val materialDao: MaterialDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                falloDao = falloDao,
                materialDao = materialDao
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}