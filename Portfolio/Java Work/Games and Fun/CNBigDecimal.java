import java.math.BigDecimal;
import java.math.MathContext;

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
public class CNBigDecimal
{
	private final MathContext scale = MathContext.DECIMAL32;
    /**
    * Used in <code>format(int)</code> to format the complex number as R.cis(theta), where theta is arg(z)
    */
    public static final int RCIS = 1;
    /**
    * The real, Re(z), part of the <code>ComplexNumber</code>.
    */
    private BigDecimal real;
    /**
    * The imaginary, Im(z), part of the <code>ComplexNumber</code>.
    */
    private BigDecimal imaginary;
    /**
    * Constructs a new <code>ComplexNumber</code> object with both real and imaginary parts 0 (z = 0 + 0i).
    */
    public CNBigDecimal()
    {
        real = new BigDecimal("0.0", scale);
        imaginary = new BigDecimal("0.0", scale);
    }

    /**
    * Constructs a new <code>ComplexNumber</code> object.
    * @param real the real part, Re(z), of the complex number
    * @param imaginary the imaginary part, Im(z), of the complex number
    */

    public CNBigDecimal(BigDecimal real, BigDecimal imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
    * Adds another <code>ComplexNumber</code> to the current complex number.
    * @param z the complex number to be added to the current complex number
    */

    public void add(CNBigDecimal z)
    {
        set(add(this,z));
    }

    /**
    * Subtracts another <code>ComplexNumber</code> from the current complex number.
    * @param z the complex number to be subtracted from the current complex number
    */

    public void subtractBigDecimal(CNBigDecimal z)
    {
        set(subtract(this,z));
    }

    /**
    * Multiplies another <code>ComplexNumber</code> to the current complex number.
    * @param z the complex number to be multiplied to the current complex number
    */

    public void multiply(CNBigDecimal z)
    {
        set(multiply(this,z));
    }
    /**
    * Sets the value of current complex number to the passed complex number.
    * @param z the complex number
    */
    public void set(CNBigDecimal z)
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
    public static CNBigDecimal add(CNBigDecimal z1, CNBigDecimal z2)
    {
        return new CNBigDecimal(z1.real.add(z2.real), z1.imaginary.add(z2.imaginary));
    }

    /**
    * Subtracts one <code>ComplexNumber</code> from another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 - z2).
    */  
    public static CNBigDecimal subtract(CNBigDecimal z1, CNBigDecimal z2)
    {
        return new CNBigDecimal(z1.real.subtract(z2.real), z1.imaginary.subtract(z2.imaginary));
    }
    /**
    * Multiplies one <code>ComplexNumber</code> to another.
    * @param z1 the first <code>ComplexNumber</code>.
    * @param z2 the second <code>ComplexNumber</code>.
    * @return the resultant <code>ComplexNumber</code> (z1 * z2).
    */  
    public static CNBigDecimal multiply(CNBigDecimal z1, CNBigDecimal z2)
    {
        BigDecimal _real = z1.real.multiply(z2.real).subtract(z1.imaginary.multiply(z2.imaginary));
        BigDecimal _imaginary = z1.real.multiply(z2.imaginary).add(z1.imaginary.multiply(z2.real));
        return new CNBigDecimal(_real,_imaginary);
    }
    /**
    * The real part of <code>ComplexNumber</code>
    * @return the real part of the complex number
    */
    public BigDecimal getRe()
    {
        return this.real;
    }
    /**
    * The imaginary part of <code>ComplexNumber</code>
    * @return the imaginary part of the complex number
    */
    public BigDecimal getIm()
    {
        return this.imaginary;
    }
    /**
    * The argument/phase of the current complex number.
    * @return arg(z) - the argument of current complex number
    */
}