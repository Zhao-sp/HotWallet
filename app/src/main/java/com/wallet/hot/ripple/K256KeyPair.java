package com.wallet.hot.ripple;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;

import java.math.BigInteger;

public abstract class K256KeyPair {

    public static byte[] signHash(byte[] bytes, BigInteger secret) {
        ECDSASignature sig = createECDSASignature(bytes, secret);
        byte[] der = sig.encodeToDER();
        if (!ECDSASignature.isStrictlyCanonical(der)) {
            throw new IllegalStateException("Signature is not strictly canonical");
        }
        return der;
    }

    private static ECDSASignature createECDSASignature(byte[] hash, BigInteger secret) {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(secret, SECP256K1.params());
        signer.init(true, privKey);
        BigInteger[] sigs = signer.generateSignature(hash);
        BigInteger r = sigs[0], s = sigs[1];

        BigInteger otherS = SECP256K1.order().subtract(s);
        if (s.compareTo(otherS) == 1) {
            s = otherS;
        }

        return new ECDSASignature(r, s);
    }
}
