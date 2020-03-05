#program to draw a stick figure

#main
#initialize each body part
addi $sp, $0, 8191
add $s7, $0, $0

addi $a0, $0, 30
addi $a1, $0, 100
addi $a2, $0, 20

jal Circle #head

addi $a0, $0, 30
addi $a1, $0, 80
addi $a2, $0, 30
addi $a3, $0, 30

jal Line #body

addi $a0, $0, 20
addi $a1, $0, 1
addi $a2, $0, 30
addi $a3, $0, 30

jal Line #left leg

addi $a0, $0, 40
addi $a1, $0, 1
addi $a2, $0, 30
addi $a3, $0, 30

jal Line #right leg

addi $a0, $0, 15
addi $a1, $0, 60
addi $a2, $0, 30
addi $a3, $0, 50

jal Line #left arm

addi $a0, $0, 30
addi $a1, $0, 50
addi $a2, $0, 45
addi $a3, $0, 60

jal Line #right arm

addi $a0, $0, 24
addi $a1, $0, 105
addi $a2, $0, 3

jal Circle #left eye

addi $a0, $0, 36
addi $a1, $0, 105
addi $a2, $0, 3

jal Circle #right eye

addi $a0, $0, 25
addi $a1, $0, 90
addi $a2, $0, 35
addi $a3, $0, 90

jal Line #mouth center

addi $a0, $0, 25
addi $a1, $0, 90
addi $a2, $0, 20
addi $a3, $0, 95

jal Line #mouth left

addi $a0, $0, 35
addi $a1, $0, 90
addi $a2, $0, 40
addi $a3, $0, 95

jal Line #mouth right

j end

Line: 
		sub $t4, $a3, $a1
		slt $t5, $t4, $0 #if t4 is negative
		beq $t5, $0, positive1 #if positive jump to next set
		sub $t4, $0, $t4 #if neg make positive

positive1:

		sub $t6, $a2, $a0
		slt $t7, $t6, $0 #if t6 is negative
		beq $t7, $0, positive2 #if positive jump to check
		sub $t6, $0, $t6 #if neg make positive

positive2:
		
		slt $t8, $t6, $t4 #if abs(y1-y0) > abs(x1 -x0) st = 1
	
		beq $t8, $0, notset #if st = 0 branch
		
		add $t0, $0, $a0 #save x0 in a temp reg
		add $a0, $0, $a1 #store y0 in a0
		add $a1, $0, $t0 #store x0 in a1

		
		add $t0, $0, $a2 #save x1 in a temp reg
		add $a2, $0, $a3 #store y1 in a2
		add $a3, $0, $t0 #store x1 in a3


notset:
		slt $t9, $a2, $a0
		beq $t9, $0, notset2 #if x0 < x1 branch
		
		add $t0, $0, $a0 #save x0 in a temp reg
		add $a0, $0, $a2 #store x1 in a0
		add $a2, $0, $t0 #store x0 in a2

		
		add $t0, $0, $a1 #save y0 in a temp reg
		add $a1, $0, $a3 #store y1 in a1
		add $a3, $0, $t0 #store y0 in a2

notset2:

		sub $s0, $a2, $a0 #deltax = x1 - x0
		sub $t4, $a3, $a1
		slt $t5, $t4, $0 #if t4 is negative
		beq $t5, $0, positive3 #if positive jump to next set
		sub $t4, $0, $t4 #if neg make positive
positive3:
		add $s1, $0, $t4 #deltay = abs(y1-y0)
		add $s2, $0, $0 #error = 0
		add $s3, $0, $a1 #y = y0

		slt $s4, $a1, $a3 #if y0 < y1
		beq $s4, $0, notless
		addi $s5, $0, 1 #ystep = 1
		j after

notless:
		addi $s5, $0, -1 #ystep = -1

after:

		add $s6, $0, $a0 #initialize start for loop x
		addi $t7, $a2, 1 #initialize end count = x1 + 1
		
loop:
		beq $s6, $t7, endloop
		beq $t8, $0, stnotset
		sw $s3, 0($s7) #plot y
		sw $s6, 1($s7) #plot x
		addi $s7, $s7, 2
		j after2

stnotset:
		sw $s6, 0($s7) #plot x
		sw $s3, 1($s7) #plot y
		addi $s7, $s7, 2

after2:

		add $s2, $s2, $s1 #error = error + deltay
		add $t0, $0, $0 #save a count for error in mult
		addi $t1, $0, $0 #initialize t1 to 0 for num

		
		sll $t1, $s2, 1 #error*2

		addi $t2, $t1, 1 #create a num for less or equal
		slt $t3, $s0, $t2 #if 2*error >= deltax
		beq $t3, $0, notset3
		add $s3, $s3, $s5 #y = y+step
		sub $s2, $s2, $s0 #error = error - deltax


notset3:
		addi $s6, $s6, 1
		j loop

endloop:
		jr $ra 
		


Circle:
		add $t0, $0, $0 #x = 0
		add $t1, $0, $a2 #y = r
		
		add $t4, $0, $0 #initialize t4 to 0 for final num

		sll $t4, $a2, 1 #2*r

		addi $t5, $0, 3
		sub $t6, $t5, $t4 #g = 3 - 2*r

		
		addi $t4, $0, $0 #initialize t4 to 0 for final num

		sll $t4, $a2, 2 #4*r

		addi $t5, $0, 10
		sub $t7, $t5, $t4 #diagnalInc = 10 - 4*r

		addi $s0, $0, 6 #rightInc = 6
		addi $t5, $t1, 1 #add one to y so we can have <=
while:
		slt $s1, $t0, $t5
		beq $s1, $0, whiledone

		add $s2, $a0, $t0 #xc + x
		add $s3, $a1, $t1 #yc + y
		sw $s2, 0($s7) #plot
		sw $s3, 1($s7)

		add $s2, $a0, $t0 #xc + x
		sub $s3, $a1, $t1 #yc - y
		sw $s2, 2($s7) #plot
		sw $s3, 3($s7)

		sub $s2, $a0, $t0 #xc - x
		add $s3, $a1, $t1 #yc + y
		sw $s2, 4($s7) #plot
		sw $s3, 5($s7)

		sub $s2, $a0, $t0 #xc - x
		sub $s3, $a1, $t1 #yc - y
		sw $s2, 6($s7) #plot
		sw $s3, 7($s7)

		add $s2, $a0, $t1 #xc + y
		add $s3, $a1, $t0 #yc + x
		sw $s2, 8($s7) #plot
		sw $s3, 9($s7)

		add $s2, $a0, $t1 #xc + y
		sub $s3, $a1, $t0 #yc - x
		sw $s2, 10($s7) #plot
		sw $s3, 11($s7)

		sub $s2, $a0, $t1 #xc - y
		add $s3, $a1, $t0 #yc + x
		sw $s2, 12($s7) #plot
		sw $s3, 13($s7)

		sub $s2, $a0, $t1 #xc - y
		sub $s3, $a1, $t0 #yc - x
		sw $s2, 14($s7) #plot
		sw $s3, 15($s7)

		addi $s7, $s7, 16 #increment  pointer

		slt $s5, $t6, $0
		bne $s5, $0, glessthan
		add $t6, $t6, $t7 #g+=diagonalInc
		addi $t7, $t7, 8  #diagnalInc += 8
		addi $t1, $t1, -1 #y-=1
		addi $t5, $t5, -1
		j gafter
glessthan:
		add $t6, $t6, $s0 #g += rightInc
		addi $t7, $t7, 4 #diagonalInc += 4

gafter:

		addi $s0, $s0, 4 #rightInc += 4
		addi $t0, $t0, 1 #x += 1		

		j while #jump back up to while
whiledone:
		jr $ra

end:
		




