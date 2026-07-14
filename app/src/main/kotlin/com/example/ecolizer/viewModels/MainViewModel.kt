package com.example.ecolizer.viewModels 

class MainViewModel(
    falloDao: FalloDao,
    materialDao: MaterialDao,
): ViewModel() {
    val fallos: LiveData<List<Fallo>> = falloDao.getItems()
    val materiales: LiveData<List<Material>> = materialDao.getItems()
}