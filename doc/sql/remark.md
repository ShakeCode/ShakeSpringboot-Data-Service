netstat -ano | findstr 8019
TCP    0.0.0.0:8019           0.0.0.0:0              LISTENING       4196
TCP    [::]:8019              [::]:0                 LISTENING       4196

taskkill /f /pid 4196