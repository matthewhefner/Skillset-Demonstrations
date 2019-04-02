import math

def Newt(f, fp, x0, TOL):
  x = x0
  if (f(x) == 0):
    print("Starting at", x0,"the solution to the equation is", x0,"in 0 iterations")
    return
  for i in range(20):
    fp0 = fp(x)
    if (abs(fp0) < 10 ** -7):
      print("The derivative at an iterate with value",x,"is zero.")
      return
    prev = x
    x = x - f(x) / fp0
    if (abs(x) > 10 ** 20):
      print("The iterates tend to infinity.")
      return
    if (abs(x - prev) < TOL):
      print("Starting at", x0 ,"the solution to the equation is", x,"in", i,"iterations")
      return
  print("The solution wasn’t found in 20 iterations. ")
  return

def func(x):
  return x**3-25

def funcP(x):
  return 3*x**2


print("Less than tolerance converge: ")
#Less than tolerance converge
Newt(func,funcP,3,10 ** -7)

print("More than 20 iterations: ")
#More than 20 iterations
Newt(func,funcP,30000,10 ** -7)

print("Off to infinity: ")
#Off to infinity
def func(x):
  return math.atan(x)

def funcP(x):
  return 1 / (x ** 2 + 1)

Newt(func,funcP,5,10 ** -7)

print("Derivative is smaller than 10^-7: ")
#Derivative is smaller than 10^-7
def func(x):
  return math.sin(x)

def funcP(x):
  return math.cos(x)

Newt(func,funcP,math.pi/2,10 ** -7)


#Number 2

def func(x):
  return ((9.8)*x/15)*(1-math.exp(-15/x*9))-35

def funcP(x):
  return -49*((x+135)*math.exp(-135/x)-x)/(75*x)

#The mass of the parachutist:
print("The mass of the parachutist:")
Newt(func,funcP,80,10 ** -5)

#Number 3

def func(x):
  return (x**2-4)**2-3

def funcP(x):
  return 4*x*(x**2-4)

#The root estimation:
print("The root estimation:")
Newt(func,funcP,3,10 ** -7)
#check:
print((4+3**(1/2))**(1/2))