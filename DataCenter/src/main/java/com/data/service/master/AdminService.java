package com.data.service.master;

import com.data.dao.master.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
   private AdminDao adminDao;

}
