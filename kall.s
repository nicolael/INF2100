# Code file created by Pascal2100 compiler 2015-11-24 23:11:56
        .extern write_char                         
        .extern write_int                         
        .extern write_string                         
        .globl  _main                         
        .globl  main                         
_main:                                  
main:   call    prog$kall_1             # Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
proc$f_2:                                
        enter   $40,$2                  # Start of f
        leave                           # End of f
        ret                             
prog$kall_1:                                
        enter   $32,$1                  # Start of kall
        movl    $113,%eax               #   char 113
        pushl   %eax                    #  Push param #2.
        movl    $2,%eax                 #  2
        pushl   %eax                    #  Push param #1.
        call    proc$f_2                
        addl    $8,%esp                 # Pop parameters.
        leave                           # End of kall
        ret                             
