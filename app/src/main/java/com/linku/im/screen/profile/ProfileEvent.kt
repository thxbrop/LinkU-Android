package com.linku.im.screen.profile

sealed class ProfileEvent {
    object FetchProfile : ProfileEvent()
    object SignOut : ProfileEvent()
}
