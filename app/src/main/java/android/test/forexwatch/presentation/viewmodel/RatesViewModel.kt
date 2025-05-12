package android.test.forexwatch.presentation.viewmodel


import android.test.forexwatch.core.utils.Resource
import android.test.forexwatch.domain.usecase.GetRatesUseCase
import android.test.forexwatch.presentation.state.RatesUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val getRatesUseCase: GetRatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RatesUiState>(RatesUiState.Loading)
    val uiState: StateFlow<RatesUiState> = _uiState.asStateFlow()

    fun loadRates() {
        viewModelScope.launch {
            getRatesUseCase().collect { result ->
                _uiState.value = when (result) {
                    is Resource.Loading -> RatesUiState.Loading
                    is Resource.Success -> RatesUiState.Success(result.data)
                    is Resource.Error -> RatesUiState.Error(result.message)
                }
            }
        }
    }
}
