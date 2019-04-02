/**
 * <code>ComplexNumber</code> is a class which implements complex numbers in Java. 
 * It includes basic operations that can be performed on complex numbers such as,
 * addition, subtraction, multiplication, conjugate, modulus and squaring. 
 * The data type for Complex Numbers.
 * <br /><br />
 * The features of this library include:<br />
 * <ul>
 * <li>Arithmetic Operations (addition, subtraction, multiplication, division)</li>
 * <li>Complex Specific Operations - Conjugate, Inverse, Absolute/Magnitude, Argument/Phase</li>
 * <li>Trigonometric Operations - sin, cos, tan, cot, sec, cosec</li>
 * <li>Mathematical Functions - exp</li>
 * <li>Complex Parsing of type x+yi</li>
 * </ul>
 * 
 * @author      Abdul Fatir
 * @version     1.1
 * 
 */
public class CN
{
    /**
    * Used in <code>format(int)</code> to format the complex number as x+yi
    */
    public static final int XY = 0;
    /**
    * Used in <code>format(int)</code> to format the complex number as R.cis(theta), where theta is arg(z)
    */
    public static final int RCIS = 1;
    /**
    * The real, Re(z), part of the <code>ComplexNumber</code>.
    */
    private double real;
    /**
    * The imaginary, Im(z), part of the <code>ComplexNumber</code>.
    */
    private double imaginary;
    /**
    * Constructs a new <code>ComplexNumber</code> object with both real and imaginary parts 0 (z = 0 + 0i).
    */
    public CN()
    {
        real = 0.0;
        imaginary = 0.0;
    }

    /**
    * Constructs a new <code>ComplexNumber</code> object.
    * @param real the real part, Re(z), of the complex number
    * @param imaginary the imaginary part, Im(z), of the complex number
    */

    public CN(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
    * Adds another <code>ComplexNumber</code> to the current complex number.
    * @param z the complex number to be added to the current complex number
    */

    public void add(CN z)
    {
        set(add(this,z));
    }

    /**
    * Subtracts another <code>ComplexNumber</code> from the current complex number.
    * @param z the complex number to be subtracted from the current complex number
    */

    public void subtract(CN z)
    {
        set(subtract(this,z));
    }

    /**
    * Multiplies another <code>ComplexNumber</code> to the current complex number.
    * @param z the complex number to be multiplied to the current complex number
    */

    public void multiply(CN z)
    {
        set(multiply(this,z));
    }
    /**
    * Divides the current <code>ComplexNumber</code> by another <code>ComplexNumber</code>.
    * @param z the divisor
    */  
    public void divide(CN z)
    {
        set(divide(this,z));
    }
    /**
    * Sets the value of current complex number to the passed complex number.
    * @param z the complex number
    */
    public void set(CN z)
    {
        this.real = z.real;
        this.imaginary = z.imaginary;
    }
    /**
    * Adds two <code>ComplexNumber</code>.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 + z2).
    */
    public static CN add(CN z1, CN z2)
    {
        return new CN(z1.real + z2.real, z1.imaginary + z2.imaginary);
    }

    /**
    * Subtracts one <code>ComplexNumber</code> from another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 - z2).
    */  
    public static CN subtract(CN z1, CN z2)
    {
        return new CN(z1.real - z2.real, z1.imaginary - z2.imaginary);
    }
    /**
    * Multiplies one <code>ComplexNumber</code> to another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 * z2).
    */  
    public static CN multiply(CN z1, CN z2)
    {
        double _real = z1.real*z2.real - z1.imaginary*z2.imaginary;
        double _imaginary = z1.real*z2.imaginary + z1.imaginary*z2.real;
        return new CN(_real,_imaginary);
    }
    /**
    * Divides one <code>ComplexNumber</code> by another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 / z2).
    */      
    public static CN divide(CN z1, CN z2)
    {
        CN output = multiply(z1,z2.conjugate());
        double div = Math.pow(z2.mod(),2);
        return new CN(output.real/div,output.imaginary/div);
    }

    /**
    * The complex conjugate of the current complex number.
    * @return a <code>ComplexNumber</code> object which is the conjugate of the current complex number
    */

    public CN conjugate()
    {
        return new CN(this.real,-this.imaginary);
    }

    /**
    * The modulus, magnitude or the absolute value of current complex number.
    * @return the magnitude or modulus of current complex number
    */

    public double mod()
    {
        return Math.sqrt(Math.pow(this.real,2) + Math.pow(this.imaginary,2));
    }

    /**
    * The square of the current complex number.
    * @return a <code>ComplexNumber</code> which is the square of the current complex number.
    */

    public CN square()
    {
        double _real = this.real*this.real - this.imaginary*this.imaginary;
        double _imaginary = 2*this.real*this.imaginary;
        return new CN(_real,_imaginary);
    }
    /**
    * @return the complex number in x + yi format
    */
    @Override
    public String toString()
    {
        String re = this.real+"";
        String im = "";
        if(this.imaginary < 0)
            im = this.imaginary+"i";
        else
            im = "+"+this.imaginary+"i";
        return re+im;
    }
    /**
    * Calculates the exponential of the <code>ComplexNumber</code>
    * @param z The input complex number
    * @return a <code>ComplexNumber</code> which is e^(input z)
    */
    public static CN exp(CN z)
    {
        double a = z.real;
        double b = z.imaginary;
        double r = Math.exp(a);
        a = r*Math.cos(b);
        b = r*Math.sin(b);
        return new CN(a,b);
    }
    /**
    * Calculates the <code>ComplexNumber</code> to the passed integer power.
    * @param z The input complex number
    * @param power The power.
    * @return a <code>ComplexNumber</code> which is (z)^power
    */
    public static CN pow(CN z, int power)
    {
        CN output = new CN(z.getRe(),z.getIm());
        for(int i = 1; i < power; i++)
        {
            double _real = output.real*z.real - output.imaginary*z.imaginary;
            double _imaginary = output.real*z.imaginary + output.imaginary*z.real;
            output = new CN(_real,_imaginary);
        }
        return output;
    }
    /**
    * Calculates the sine of the <code>ComplexNumber</code>
    * @param z the input complex number
    * @return a <code>ComplexNumber</code> which is the sine of z.
    */
    public static CN sin(CN z)
    {
        double x = Math.exp(z.imaginary);
        double x_inv = 1/x;
        double r = Math.sin(z.real) * (x + x_inv)/2;
        double i = Math.cos(z.real) * (x - x_inv)/2;
        return new CN(r,i);
    }
    /**
    * Calculates the cosine of the <code>ComplexNumber</code>
    * @param z the input complex number
    * @return a <code>ComplexNumber</code> which is the cosine of z.
    */
    public static CN cos(CN z)
    {
        double x = Math.exp(z.imaginary);
        double x_inv = 1/x;
        double r = Math.cos(z.real) * (x + x_inv)/2;
        double i = -Math.sin(z.real) * (x - x_inv)/2;
        return new CN(r,i);
    }
    /**
    * Calculates the tangent of the <code>ComplexNumber</code>
    * @param z the input complex number
    * @return a <code>ComplexNumber</code> which is the tangent of z.
    */
    public static CN tan(CN z)
    {
        return divide(sin(z),cos(z));
    }
    /**
    * Calculates the co-tangent of the <code>ComplexNumber</code>
    * @param z the input complex number
    * @return a <code>ComplexNumber</code> which is the co-tangent of z.
    */
    public static CN cot(CN z)
    {
        return divide(new CN(1,0),tan(z));
    }
    /**
    * Calculates the secant of the <code>ComplexNumber</code>
    * @param z the input complex number
    * @return a <code>ComplexNumber</code> which is the secant of z.
    */
    public static CN sec(CN z)
    {
        return divide(new CN(1,0),cos(z));
    }
    /**
    * Calculates the co-secant of the <code>ComplexNumber</code>
    * @param z the input complex number
    * @return a <code>ComplexNumber</code> which is the co-secant of z.
    */
    public static CN cosec(CN z)
    {
        return divide(new CN(1,0),sin(z));
    }
    /**
    * The real part of <code>ComplexNumber</code>
    * @return the real part of the complex number
    */
    public double getRe()
    {
        return this.real;
    }
    /**
    * The imaginary part of <code>ComplexNumber</code>
    * @return the imaginary part of the complex number
    */
    public double getIm()
    {
        return this.imaginary;
    }
    /**
    * The argument/phase of the current complex number.
    * @return arg(z) - the argument of current complex number
    */
    public double getArg()
    {
        return Math.atan2(imaginary,real);
    }
    /**
    * Parses the <code>String</code> as a <code>ComplexNumber</code> of type x+yi.
    * @param s the input complex number as string
    * @return a <code>ComplexNumber</code> which is represented by the string.
    */
    public static CN parseComplex(String s)
    {
        s = s.replaceAll(" ","");
        CN parsed = null;
        if(s.contains(String.valueOf("+")) || (s.contains(String.valueOf("-")) && s.lastIndexOf('-') > 0))
        {
            String re = "";
            String im = "";
            s = s.replaceAll("i","");
            s = s.replaceAll("I","");
            if(s.indexOf('+') > 0)
            {
                re = s.substring(0,s.indexOf('+'));
                im = s.substring(s.indexOf('+')+1,s.length());
                parsed = new CN(Double.parseDouble(re),Double.parseDouble(im));
            }
            else if(s.lastIndexOf('-') > 0)
            {
                re = s.substring(0,s.lastIndexOf('-'));
                im = s.substring(s.lastIndexOf('-')+1,s.length());
                parsed = new CN(Double.parseDouble(re),-Double.parseDouble(im));
            }
        }
        else
        {
            // Pure imaginary number
            if(s.endsWith("i") || s.endsWith("I"))
            {
                s = s.replaceAll("i","");
                s = s.replaceAll("I","");
                parsed = new CN(0, Double.parseDouble(s));
            }
            // Pure real number
            else
            {
                parsed = new CN(Double.parseDouble(s),0);
            }
        }
        return parsed;
    }
    /**
    * Checks if the passed <code>ComplexNumber</code> is equal to the current.
    * @param z the complex number to be checked
    * @return true if they are equal, false otherwise
    */
    @Override
    public final boolean equals(Object z) 
    {
        if (!(z instanceof CN))
            return false;
        CN a = (CN) z;
        return (real == a.real) && (imaginary == a.imaginary);
    }
    /**
    * The inverse/reciprocal of the complex number.
    * @return the reciprocal of current complex number.
    */
    public CN inverse()
    {
        return divide(new CN(1,0),this);
    }
    /**
    * Formats the Complex number as x+yi or r.cis(theta)
    * @param format_id the format ID <code>ComplexNumber.XY</code> or <code>ComplexNumber.RCIS</code>.
    * @return a string representation of the complex number
    * @throws IllegalArgumentException if the format_id does not match.
    */
    public String format(int format_id) throws IllegalArgumentException
    {
        String out = "";
        if(format_id == XY)
            out = toString();
        else if(format_id == RCIS)
        {
            out = mod()+" cis("+getArg()+")";
        }
        else
        {
            throw new IllegalArgumentException("Unknown Complex Number format.");
        }
        return out;
    }
}