package com.wallet.hot.utils.btc;

import android.net.Uri;
import android.util.Log;

import com.wallet.R;
import com.wallet.cold.utils.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.bitcoinj.core.Base58;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.DSA;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.crypto.params.ParametersWithRandom;
import org.spongycastle.crypto.signers.DSAKCalculator;
import org.spongycastle.crypto.signers.RandomDSAKCalculator;
import org.spongycastle.math.ec.ECAlgorithms;
import org.spongycastle.math.ec.ECConstants;
import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECMultiplier;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.math.ec.FixedPointCombMultiplier;

import cn.tass.exceptions.TAException;
import cn.tass.hsmApi.blockChainApi.blockChainApi;

import static com.google.common.io.Resources.getResource;

/**
 * EC-DSA as described in X9.62
 */
public class ECDSASigner
        implements ECConstants, DSA
{
    private final DSAKCalculator kCalculator;

    private ECKeyParameters key;
    private SecureRandom    random;

    /**
     * Default configuration, random K values.
     */
    public ECDSASigner()
    {
        this.kCalculator = new RandomDSAKCalculator();
    }

    /**
     * Configuration with an alternate, possibly deterministic calculator of K.
     *
     * @param kCalculator a K value calculator.
     */
    public ECDSASigner(DSAKCalculator kCalculator)
    {
        this.kCalculator = kCalculator;
    }

    public void init(
            boolean                 forSigning,
            CipherParameters        param)
    {
        SecureRandom providedRandom = null;

        if (forSigning)
        {
            if (param instanceof ParametersWithRandom)
            {
                ParametersWithRandom rParam = (ParametersWithRandom)param;

                this.key = (ECPrivateKeyParameters)rParam.getParameters();
                providedRandom = rParam.getRandom();
            }
            else
            {
                this.key = (ECPrivateKeyParameters)param;
            }
        }
        else
        {
            this.key = (ECPublicKeyParameters)param;
        }

        this.random = initSecureRandom(forSigning && !kCalculator.isDeterministic(), providedRandom);
    }

    // 5.3 pg 28
    /**
     * generate a signature for the given message using the key we were
     * initialised with. For conventional DSA the message should be a SHA-1
     * hash of the message of interest.
     *
     * @param message the message that will be verified later.
     */
    public BigInteger[] generateSignature(byte[] message) {
        ECDomainParameters ec = key.getParameters();
        BigInteger n = ec.getN();
        BigInteger e = calculateE(n, message);
        BigInteger d = ((ECPrivateKeyParameters)key).getD();
        if (kCalculator.isDeterministic()) {
            kCalculator.init(n, d, message);
        } else {
            kCalculator.init(n, random);
        }
        BigInteger r, s ,k;
        ECMultiplier basePointMultiplier = createBasePointMultiplier();
        // 5.3.2
        do // generate s
        {
            do // generate r
            {
                k = kCalculator.nextK();
                ECPoint p = basePointMultiplier.multiply(ec.getG(), k).normalize();
                // 5.3.3
                r = p.getAffineXCoord().toBigInteger().mod(n);
            }
            while (r.equals(ZERO));
            s = k.modInverse(n).multiply(e.add(d.multiply(r))).mod(n);
            Log.d("k",k.toString());
            Log.d("k--16",new BigInteger(k.toString(),10).toString(16));
        }
        while (s.equals(ZERO));
        Log.d("n",n.toString());
        Log.d("e",e.toString());
        Log.d("d",d.toString());
        Log.d("r",r.toString());
        Log.d("s",s.toString());
        Log.d("n--16",new BigInteger(n.toString(),10).toString(16));
        Log.d("e--16",new BigInteger(e.toString(),10).toString(16));
        Log.d("d--16",new BigInteger(d.toString(),10).toString(16));
        Log.d("r--16",new BigInteger(r.toString(),10).toString(16));
        Log.d("s--16",new BigInteger(s.toString(),10).toString(16));
        Uri uri= Uri.parse("android.resource://com.hotwallet/"+R.raw.cacipher);
        String strConfig = uri.getPath();
        try {
            blockChainApi api = blockChainApi.getInstance(strConfig);
            byte[] bytes1 = api.B5_signatureWithBlockchain("xprv9s21ZrQH143K3QTDL4LXw2F7HEK3wJUD2nW2nRk4stbPy6cq3jPPqjiChjh27czWF7om2Wz9CeZEoYMfMPu7pkpmHWbmnEuSgC8EyHgtmcW",message,toByteArray(r),toByteArray(k),toByteArray(n));
            s = new BigInteger(1, bytes1);
        } catch (TAException ex) {
            ex.printStackTrace();
        }
//        s = new BigInteger("c616f5e8464780a83e2459e0f6b20b54e1698b72ece52b61fa559685504af617",16);
        return new BigInteger[]{ r, s };
    }

    public static byte[] toByteArray(BigInteger bi) {
        byte[] array = bi.toByteArray();
        if (array[0] == 0) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        return array;
    }

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    // 5.4 pg 29
    /**
     * return true if the value r and s represent a DSA signature for
     * the passed in message (for standard DSA the message should be
     * a SHA-1 hash of the real message to be verified).
     */
    public boolean verifySignature(
            byte[]      message,
            BigInteger  r,
            BigInteger  s)
    {
        ECDomainParameters ec = key.getParameters();
        BigInteger n = ec.getN();
        BigInteger e = calculateE(n, message);

        // r in the range [1,n-1]
        if (r.compareTo(ONE) < 0 || r.compareTo(n) >= 0)
        {
            return false;
        }

        // s in the range [1,n-1]
        if (s.compareTo(ONE) < 0 || s.compareTo(n) >= 0)
        {
            return false;
        }

        BigInteger c = s.modInverse(n);

        BigInteger u1 = e.multiply(c).mod(n);
        BigInteger u2 = r.multiply(c).mod(n);

        ECPoint G = ec.getG();
        ECPoint Q = ((ECPublicKeyParameters)key).getQ();

        ECPoint point = ECAlgorithms.sumOfTwoMultiplies(G, u1, Q, u2);

        // components must be bogus.
        if (point.isInfinity())
        {
            return false;
        }

        /*
         * If possible, avoid normalizing the point (to save a modular inversion in the curve field).
         *
         * There are ~cofactor elements of the curve field that reduce (modulo the group order) to 'r'.
         * If the cofactor is known and small, we generate those possible field values and project each
         * of them to the same "denominator" (depending on the particular projective coordinates in use)
         * as the calculated point.X. If any of the projected values matches point.X, then we have:
         *     (point.X / Denominator mod p) mod n == r
         * as required, and verification succeeds.
         *
         * Based on an original idea by Gregory Maxwell (https://github.com/gmaxwell), as implemented in
         * the libsecp256k1 project (https://github.com/bitcoin/secp256k1).
         */
        ECCurve curve = point.getCurve();
        if (curve != null)
        {
            BigInteger cofactor = curve.getCofactor();
            if (cofactor != null && cofactor.compareTo(EIGHT) <= 0)
            {
                ECFieldElement D = getDenominator(curve.getCoordinateSystem(), point);
                if (D != null && !D.isZero())
                {
                    ECFieldElement X = point.getXCoord();
                    while (curve.isValidFieldElement(r))
                    {
                        ECFieldElement R = curve.fromBigInteger(r).multiply(D);
                        if (R.equals(X))
                        {
                            return true;
                        }
                        r = r.add(n);
                    }
                    return false;
                }
            }
        }

        BigInteger v = point.normalize().getAffineXCoord().toBigInteger().mod(n);
        return v.equals(r);
    }

    protected BigInteger calculateE(BigInteger n, byte[] message)
    {
        int log2n = n.bitLength();
        int messageBitLength = message.length * 8;

        BigInteger e = new BigInteger(1, message);
        if (log2n < messageBitLength)
        {
            e = e.shiftRight(messageBitLength - log2n);
        }
        return e;
    }

    protected ECMultiplier createBasePointMultiplier()
    {
        return new FixedPointCombMultiplier();
    }

    protected ECFieldElement getDenominator(int coordinateSystem, ECPoint p)
    {
        switch (coordinateSystem)
        {
            case ECCurve.COORD_HOMOGENEOUS:
            case ECCurve.COORD_LAMBDA_PROJECTIVE:
            case ECCurve.COORD_SKEWED:
                return p.getZCoord(0);
            case ECCurve.COORD_JACOBIAN:
            case ECCurve.COORD_JACOBIAN_CHUDNOVSKY:
            case ECCurve.COORD_JACOBIAN_MODIFIED:
                return p.getZCoord(0).square();
            default:
                return null;
        }
    }

    protected SecureRandom initSecureRandom(boolean needed, SecureRandom provided)
    {
        return !needed ? null : (provided != null) ? provided : new SecureRandom();
    }
}
