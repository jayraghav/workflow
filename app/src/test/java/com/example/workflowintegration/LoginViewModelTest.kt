package com.example.workflowintegration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.workflowintegration.viewModel.LoginViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // LiveData को sync बनाता है

    private lateinit var viewModel: LoginViewModel
    private lateinit var observer: Observer<LoginViewModel.LoginResult>

    @Before
    fun setup() {
        viewModel = LoginViewModel()
        observer = mock(Observer::class.java) as Observer<LoginViewModel.LoginResult>
        viewModel.loginResult.observeForever(observer)
    }

    @Test
    fun `login success with correct credentials`() {
        viewModel.login("admin", "1234")
        verify(observer).onChanged(LoginViewModel.LoginResult.Success)
    }

    @Test
    fun `login fails with empty username or password`() {
        viewModel.login("", "1234")
        verify(observer).onChanged(LoginViewModel.LoginResult.Error("Username or password cannot be empty"))

        viewModel.login("admin", "")
        verify(observer).onChanged(LoginViewModel.LoginResult.Error("Username or password cannot be empty"))
    }

    @Test
    fun `login fails with invalid credentials`() {
        viewModel.login("user", "wrongpass")
        verify(observer).onChanged(LoginViewModel.LoginResult.Error("Invalid credentials"))
    }
}
