package com.example.demo.doSomething;

import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoSomethingService {

    private final UserRepository userRepository;

}
