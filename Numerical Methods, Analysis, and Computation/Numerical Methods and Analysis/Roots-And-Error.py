def cuberoot2(x0):
  x = x0
  i = 0
  while True:
    nextIt = (1/3)*(2*x+2/(x**2))
    if (abs(nextIt - x) <= 10**-7):
      break
    else:
      i += 1
      x = nextIt
  print("The sequence starting at", x0, "converges to", x,"in", i, "iterations.")

cuberoot2(20)

def nesty(x):
  f = (x - 1) ** 5
  g = x ** 5 - 5 * x ** 4 + 10 * x ** 3 - 10 * x ** 2 + 5 * x -1
  h = x * (5 - x * (10 - x * (10 - x * (5 - x)))) - 1
  print("x =", x, ", f(x) =", f, ", g(x) =", g, ", h(x) =", h)

for i in range(1,8):
  nesty(1 + 10 ** -i)

#the answer *should* be (10^-n)^5 = 10 ^ -5n.  With n ranging from 1 to 7, that goes as low as 10 ^ -35!  This means that we want to minimize our loss of significance or face massive relative error
#f minimized the number of operations (in particular subtraction) to the answer, thereby minimizing loss of significance and the relative error
#therefore f output the best results
#on the other hand, g and h had more subtraction, resulting in greater loss of significance, worse relative error, and worse results