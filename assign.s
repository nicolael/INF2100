# Code file created by Pascal2100 compiler 2015-11-25 10:27:02
        .extern write_char                         
        .extern write_int                         
        .extern write_string                         
        .globl  _main                         
        .globl  main                         
_main:                                  
main:   call    prog$assign_1           # Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
prog$assign_1:                                
        enter   $40,$1                  # Start of assign
        movl    -4(%ebp),%edx           
        movl    -36(%edx),%eax          # v
        movl    -4(%ebp),%edx           
        movl    %eax,-40(%edx)          # w :=
        leave                           # End of assign
        ret                             
