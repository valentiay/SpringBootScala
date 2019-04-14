package com.ironworkman.service

import org.springframework.stereotype.Service

@Service
class MyService {
  def getMessage: String = {
    s"The service says: "
  }
}
