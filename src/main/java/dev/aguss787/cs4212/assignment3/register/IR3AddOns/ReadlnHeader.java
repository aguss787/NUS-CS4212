package dev.aguss787.cs4212.assignment3.register.IR3AddOns;

import dev.aguss787.cs4212.assignment2.IR3.statement.Statement;
import dev.aguss787.cs4212.assignment3.asm.AssemblyBuilder;
import dev.aguss787.cs4212.assignment3.asm.statement.Template;

import java.util.List;

/**
 * Readln arm asm code, for int only
 * Source: https://www.rosettacode.org/wiki/User_input/Text#ARM_Assembly
 */
public class ReadlnHeader extends Statement {
    private final String template = "/* ARM assembly Raspberry PI  */\n" +
            "/*  program inputText.s   */\n" +
            " \n" +
            "/* Constantes    */\n" +
            ".equ BUFFERSIZE,   100\n" +
            ".equ STDIN,  0     @ Linux input console\n" +
            ".equ STDOUT, 1     @ Linux output console\n" +
            ".equ EXIT,   1     @ Linux syscall\n" +
            ".equ READ,   3     @ Linux syscall\n" +
            ".equ WRITE,  4     @ Linux syscall\n" +
            "/* Initialized data */\n" +
            ".data\n" +
            "szMessDeb: .asciz \"Enter text : \\n\"\n" +
            "szMessNum: .asciz \"Enter number : \\n\"\n" +
            "szCarriageReturn:  .asciz \"\\n\"\n" +
            "szMessErrDep:  .asciz  \"Too large: overflow 32 bits.\\n\"\n" +
            "\n" +
            "/* UnInitialized data */\n" +
            ".bss \n" +
            "sBuffer:    .skip    BUFFERSIZE\n" +
            " \n" +
            "/*  code section */\n" +
            ".text\n" +
            ".global _readln \n" +
            "_readln:                /* entry of program  */\n" +
            "    push {fp,lr}    /* saves 2 registers */\n" +
            "    push {r1,r2,r7}    \t\t/* save others registers */\n" +
            "\n" +
            "    mov r0,#STDIN         @ Linux input console\n" +
            "    ldr r1,iAdrsBuffer   @ buffer address \n" +
            "    mov r2,#BUFFERSIZE   @ buffer size \n" +
            "    mov r7, #READ         @ request to read datas\n" +
            "    swi 0                  @ call system\n" +
            "    ldr r1,iAdrsBuffer    @ buffer address \n" +
            "    mov r2,#0                @ end of string\n" +
            "    strb r2,[r1,r0]         @ store byte at the end of input string (r0\n" +
            "    @ \n" +
            "    ldr r0,iAdrsBuffer    @ buffer address\n" +
            "    bl conversionAtoD    @ conversion string in number in r0\n" +
            "\t\n" +
            "    pop {r1,r2,r7}    \t\t/* save others registers */\n" +
            "    pop {fp,pc}                 @restaur 2 registers\n" +
            "\n" +
            "iAdrszMessDeb:  .int szMessDeb\n" +
            "iAdrszMessNum: .int  szMessNum\n" +
            "iAdrsBuffer:   .int  sBuffer\n" +
            "iAdrszCarriageReturn:  .int  szCarriageReturn\n" +
            "/******************************************************************/\n" +
            "/*     display text with size calculation                         */ \n" +
            "/******************************************************************/\n" +
            "/* r0 contains the address of the message */\n" +
            "affichageMess:\n" +
            "    push {fp,lr}    \t\t\t/* save  registres */ \n" +
            "    push {r0,r1,r2,r7}    \t\t/* save others registers */\n" +
            "    mov r2,#0   \t\t\t\t/* counter length */\n" +
            "1:      \t/* loop length calculation */\n" +
            "    ldrb r1,[r0,r2]  \t\t\t/* read octet start position + index */\n" +
            "    cmp r1,#0       \t\t\t/* if 0 its over */\n" +
            "    addne r2,r2,#1   \t\t\t/* else add 1 in the length */\n" +
            "    bne 1b          \t\t\t/* and loop */\n" +
            "                                /* so here r2 contains the length of the message */\n" +
            "    mov r1,r0        \t\t\t/* address message in r1 */\n" +
            "    mov r0,#STDOUT      \t\t/* code to write to the standard output Linux */\n" +
            "    mov r7, #WRITE             /* code call system \"write\" */\n" +
            "    swi #0                      /* call systeme */\n" +
            "    pop {r0,r1,r2,r7}     \t\t/* restaur others registers */\n" +
            "    pop {fp,lr}    \t\t\t\t/* restaur des  2 registres */ \n" +
            "    bx lr\t        \t\t\t/* return  */\n" +
            " \n" +
            "/******************************************************************/\n" +
            "/*     Convert a string to a number stored in a registry          */ \n" +
            "/******************************************************************/\n" +
            "/* r0 contains the address of the area terminated by 0 or 0A */\n" +
            "/* r0 returns a number                           */\n" +
            "conversionAtoD:\n" +
            "    push {fp,lr}         @ save 2 registers \n" +
            "    push {a2-a4, v1-v6}      @ save others registers \n" +
            "    mov r1,#0\n" +
            "    mov r2,#10           @ factor \n" +
            "    mov r3,#0            @ counter \n" +
            "    mov r4,r0            @ save address string -> r4 \n" +
            "    mov r6,#0            @ positive sign by default \n" +
            "    mov r0,#0            @ initialization to 0 \n" +
            "1:     /* early space elimination loop */\n" +
            "    ldrb r5,[r4,r3]     @ loading in r5 of the byte located at the beginning + the position \n" +
            "    cmp r5,#0            @ end of string -> end routine\n" +
            "    beq 100f\n" +
            "    cmp r5,#0x0A        @ end of string -> end routine\n" +
            "    beq 100f\n" +
            "    cmp r5,#' '          @ space ? \n" +
            "    addeq r3,r3,#1      @ yes we loop by moving one byte \n" +
            "    beq 1b\n" +
            "    cmp r5,#'-'          @ first character is -    \n" +
            "    moveq r6,#1         @  1 -> r6\n" +
            "    beq 3f              @ then move on to the next position \n" +
            "2:   /* beginning of digit processing loop */\n" +
            "    cmp r5,#'0'          @ character is not a number \n" +
            "    blt 3f\n" +
            "    cmp r5,#'9'          @ character is not a number\n" +
            "    bgt 3f\n" +
            "    /* character is a number */\n" +
            "    sub r5,#48\n" +
            "    ldr r1,iMaxi       @ check the overflow of the register    \n" +
            "    cmp r0,r1\n" +
            "    bgt 99f            @ overflow error\n" +
            "    mul r0,r2,r0         @ multiply par factor 10 \n" +
            "    add r0,r5            @ add to  r0 \n" +
            "3:\n" +
            "    add r3,r3,#1         @ advance to the next position \n" +
            "    ldrb r5,[r4,r3]     @ load byte \n" +
            "    cmp r5,#0            @ end of string -> end routine\n" +
            "    beq 4f\n" +
            "    cmp r5,#0x0A            @ end of string -> end routine\n" +
            "    beq 4f\n" +
            "    b 2b                 @ loop \n" +
            "4:\n" +
            "    cmp r6,#1            @ test r6 for sign \n" +
            "    moveq r1,#-1\n" +
            "    muleq r0,r1,r0       @ if negatif, multiply par -1 \n" +
            "    b 100f\n" +
            "99:  /* overflow error */\n" +
            "    ldr r0,=szMessErrDep\n" +
            "    bl   affichageMess\n" +
            "    mov r0,#0      @ return  zero  if error\n" +
            "100:\n" +
            "    pop {a2-a4, v1-v6}          @ restaur other registers \n" +
            "    pop {fp,lr}          @ restaur   2 registers \n" +
            "    bx lr                 @return procedure \n" +
            "\n" +
            "iMaxi: .int 1073741824\n";
    @Override
    public void buildAsm(AssemblyBuilder assemblyBuilder) {
        assemblyBuilder.add(new Template(template));
    }

    @Override
    public String toString() {
        return template;
    }
}
