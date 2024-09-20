package com.LoginPage.creating.Login.page.repositiory;

import com.LoginPage.creating.Login.page.model.UserCredential;
import com.LoginPage.creating.Login.page.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    List<UserInfo> findByUserCredential(UserCredential userCredential);


}