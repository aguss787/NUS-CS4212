
	.arch armv7-a
	.data 
str_1:
	.asciz "Okay"
	.data 
str_2:
	.asciz "%d\n"
	.text 
	.global _MainC1
	.type _MainC1, %function
_MainC1:
	push {v1, v2, v3, v4, v5, fp, lr}
	mov fp, sp
	sub sp, sp, #8
	str a1, [fp, #0]
	ldr v5, [fp, #-4]
	mov v5, #10
	ldr a1, =str_1
	bl puts(PLT)
	mov a2, v5
	ldr a1, =str_2
	bl printf(PLT)
_MainC1_func_ret:
	add sp, sp, #8
	pop {v1, v2, v3, v4, v5, fp, pc}
	.text 
	.global main
	.type main, %function
main:
	push {v1, v2, v3, v4, v5, fp, lr}
	mov fp, sp
	sub sp, sp, #0
	bl _MainC1(PLT)
	mov a1, #0
main_func_ret:
	add sp, sp, #0
	pop {v1, v2, v3, v4, v5, fp, pc}
