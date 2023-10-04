package com.myapp.domain.use_cases

import android.content.Context
import android.net.Uri
import com.myapp.domain.repository.UserRepository
import com.myapp.presentation.util.ResultHappen
import javax.inject.Inject

class GetImageUrlUseCase @Inject constructor(private val userRepository: UserRepository){

    suspend fun execute(
        uri: Uri,
        context: Context,
        onComplete: (String) -> Unit,
        onError: (String) -> Unit
    ){
        userRepository.getImageURLFromStorage(uri, context).let { result->
            when (result) {
                is ResultHappen.Success -> {
                    onComplete(result.data.toString())
                }
                is ResultHappen.Error -> {
                    onError(result.message.toString())
                }
            }
        }
    }
}