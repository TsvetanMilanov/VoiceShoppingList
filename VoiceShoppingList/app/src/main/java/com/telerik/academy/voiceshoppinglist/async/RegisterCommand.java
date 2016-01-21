package com.telerik.academy.voiceshoppinglist.async;

import com.telerik.academy.voiceshoppinglist.remote.models.UserRegisterResponseModel;

public interface RegisterCommand {
    void execute(UserRegisterResponseModel user);
}
