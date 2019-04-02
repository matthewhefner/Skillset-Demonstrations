import math

def pseudo1():
  for i in range(1, 21):
    #First we get an x, a number that should get closer to 2
    #by adding a decreasing power of 8 (1 / 8 ^ i = 8 ^ -i),
    #*theoretically* approaching, but never reaching, 2 (goes to purpose of program)
    x = 2 + 1.0 / (8 ** i)
    #Then we find y, the difference of the atan of a number that is, as stated above,
    #supposed to be approaching 2, and atan of 2.  This should then be, theoretically,
    #approaching 0, but never reaching, 0 (again, stating purpose)
    y = math.atan(x) - math.atan(2)
    #Then we find z, where we scale the above difference y by the reciprocal
    #Of what we initially added to 2 to get x (8 ^ i), which gets larger as
    #y gets smaller, hopefully giving increasingly better approximations of
    #the limit of (8^x)(arctan(2 + 8^(-x)) - arctan(2)) as x -> 0
    z = (8 ** i) * y
    #Output x,y,z...
    print(x, y, z)

pseudo1()
#When i = 18, 8^-18 becomes too small for the machine to store.
#This results in anything > 17 having an x = 2, resulting in a 
#0 for y and a 0 for z.
#The result of operations being affected crops up slightly earlier
#in z, where i = 16 and i = 17 whose results begin to strongly deviate from
#previous zs.
#As for achieving the goal, The program is generally well behaved up until i = 16,
#so, you could say 0.2 *may be* a good approximation of the limit.

def pseudo2():
  #Start your guess for the machine epsilon at 2^0 = 1
  epsi = 1
  while(1 < 1 + epsi): #if 1 + 2^n can be represented on the machine...
    #let epsilon become 2^(n-1), which is the max rounding error
    #when storing on the machine for mach-epsi = 2^n
    epsi = epsi / 2
    #print max rounding error
    print(epsi)

pseudo2()
#This should (and does) print the max rounding error of the machine
#at a store.

def mult15(n):
  bigPow = 0
  s = 0
  while(15 ** (bigPow + 1) < n): #Find largest power of 15 within n
    bigPow += 1
  if (bigPow > 0): #we don't want s = 1 !
    s = 15 ** bigPow
  else:
    s = 0 #that's better :)
  for power in range(bigPow, 0, -1): #for every power from bigPow down to 15 ^ 1,
    while(s + 15 ** power < n): #add however many multiples of that power will fit
      s += 15 ** power
  print("The largest multiple of 15 that is smaller than", n, "is", s)

mult15(11)
mult15(16)
mult15(93684)
mult15(4897487)
mult15(618646576543)
#Since we couldn't do n - (n % 15), which (assuming mod is in the CPU's ISA) is constant
#time complexity, I wanted to think of something with better time complexity than
#just linear.  I think this is O(log(n)).
