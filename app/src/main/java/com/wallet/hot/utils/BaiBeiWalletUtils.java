package com.wallet.hot.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LogCook;
import com.wallet.cold.utils.Utils;
import com.wallet.hot.app.HotTransfer;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import static com.wallet.cold.utils.Utils.bytesToHexString;
import static com.wallet.cold.utils.Utils.strTo16;
import static org.bitcoinj.crypto.HDUtils.parsePath;
import static org.web3j.crypto.Hash.sha256;
import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;
import static org.web3j.crypto.Keys.PRIVATE_KEY_LENGTH_IN_HEX;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/12 16:56
 */
public class BaiBeiWalletUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SecureRandom secureRandom = new SecureRandom();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String generateFullNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(password, destinationDirectory, true);
    }

    public static String generateLightNewWalletFile(String password, File destinationDirectory)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CipherException, IOException {

        return generateNewWalletFile(password, destinationDirectory, false);
    }

    public static String generateNewWalletFile(
            String password, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {

        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        return generateWalletFile(password, ecKeyPair, destinationDirectory, useFullScrypt);
    }

    public static String generateWalletFile(
            String password, ECKeyPair ecKeyPair, File destinationDirectory, boolean useFullScrypt)
            throws CipherException, IOException {

        WalletFile walletFile;
        if (useFullScrypt) {
            walletFile = org.web3j.crypto.Wallet.createStandard(password, ecKeyPair);
        } else {
            walletFile = org.web3j.crypto.Wallet.createLight(password, ecKeyPair);
        }

        String fileName = getWalletFileName(walletFile);
        File destination = new File(destinationDirectory, fileName);

        objectMapper.writeValue(destination, walletFile);

        return fileName;
    }

    /**
     * Generates a BIP-39 compatible Ethereum wallet. The private key for the wallet can
     * be calculated using following algorithm:
     * <pre>
     *     Key = SHA-256(BIP_39_SEED(mnemonic, password))
     * </pre>
     *
     * @param password             Will be used for both wallet encryption and passphrase for BIP-39 seed
     * @param destinationDirectory The directory containing the wallet
     * @return A BIP-39 compatible Ethereum wallet
     * @throws CipherException if the underlying cipher is not available
     * @throws IOException     if the destination cannot be written to
     */
    public static Bip39Wallet generateBip39Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);

        String mnemonic = org.web3j.crypto.MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = org.web3j.crypto.MnemonicUtils.generateSeed(mnemonic, password);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));

        String walletFile = generateWalletFile(password, privateKey, destinationDirectory, false);

        return new Bip39Wallet(walletFile, mnemonic);
    }

    public static Credentials loadCredentials(String password, String source)
            throws IOException, CipherException {
        return loadCredentials(password, new File(source));
    }

    public static Credentials loadCredentials(String password, File source)
            throws IOException, CipherException {
        WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
        return Credentials.create(Wallet.decrypt(password, walletFile));
    }
    public static Credentials loadBip44Wallet(String password, String keystore)
            throws IOException, CipherException {
        WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);

        return Credentials.create(Wallet.decrypt(password, walletFile));
    }

    public static Credentials loadBip39Credentials(String password, String mnemonic) {
        byte[] seed = org.web3j.crypto.MnemonicUtils.generateSeed(mnemonic, password);
        return Credentials.create(ECKeyPair.create(sha256(seed)));
    }

//    public static Credentials loadBip44Credentials(String password, String mnemonic) {
//        byte[] seed = org.web3j.crypto.MnemonicUtils.generateSeed(mnemonic, null);
//        DeterministicKey keys = getDeterministicKeyBip44BySeed(seed);
//
//        byte[] privateKeyByte = keys.getPrivKeyBytes();
////        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
//        //通过私钥生成公私钥对
//        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
//
//        WalletFile walletFile = Wallet.createLight(password, keyPair);
//        Log.i("TAG", "generateBip44Wallet: 地址 = " + Keys.toChecksumAddress(walletFile.getAddress()));
//        return new BaibeiWallet(walletFile, mnemonic);
//        return Credentials.create(keyPair);
//    }

    private static String getWalletFileName(WalletFile walletFile) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        return dateFormat.format(new Date()) + walletFile.getAddress() + ".json";
    }

    public static String getDefaultKeyDirectory() {
        return getDefaultKeyDirectory(System.getProperty("os.name"));
    }

    static String getDefaultKeyDirectory(String osName1) {
        String osName = osName1.toLowerCase();

        if (osName.startsWith("mac")) {
            return String.format(
                    "%s%sLibrary%sEthereum", System.getProperty("user.home"), File.separator,
                    File.separator);
        } else if (osName.startsWith("win")) {
            return String.format("%s%sEthereum", System.getenv("APPDATA"), File.separator);
        } else {
            return String.format("%s%s.ethereum", System.getProperty("user.home"), File.separator);
        }
    }

    public static String getTestnetKeyDirectory() {
        return String.format(
                "%s%stestnet%skeystore", getDefaultKeyDirectory(), File.separator, File.separator);
    }

    public static String getMainnetKeyDirectory() {
        return String.format("%s%skeystore", getDefaultKeyDirectory(), File.separator);
    }

    public static boolean isValidPrivateKey(String privateKey) {
        String cleanPrivateKey = Numeric.cleanHexPrefix(privateKey);
        return cleanPrivateKey.length() == PRIVATE_KEY_LENGTH_IN_HEX;
    }

    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_LENGTH_IN_HEX;
    }

    private static DeterministicKey getDeterministicKeyBip44BySeed(byte[] seed) {
        // 3. 生成根私钥 root private key 树顶点的master key
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        if (Boolean.parseBoolean("true")) {
            // 根私钥进行 priB58编码,得到测试网站上显示的数据
            NetworkParameters params = MainNetParams.get();
            String priv = rootPrivateKey.serializePrivB58(params);
            Log.i("TAG", "BIP32 Extended Private Key:" + priv);
        }
        // 4. 由根私钥生成 第一个HD 钱包
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 5. 定义父路径 H则是加强 imtoken中的eth钱包进过测试发现使用的是此方式生成
        List<ChildNumber> parentPath = parsePath("M/44H/60H/0H/0");
        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/60'/0'/0/0）
        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
        return child;
    }

    public static BaibeiWallet generateBip44Wallet(String password) throws CipherException {
        //1.通过bip39生成助记词
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        if (Boolean.parseBoolean("true")) {
            Log.i("TAG", "generateBip44Wallet: 助记词 = " + mnemonic);
            Data.sethotzjc(mnemonic);
        }
        //2.生成种子
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        // 3. 生成根私钥 root private key 树顶点的master key
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        if (Boolean.parseBoolean("true")) {
            // 根私钥进行 priB58编码,得到测试网站上显示的数据
            NetworkParameters params = MainNetParams.get();
            String priv = rootPrivateKey.serializePrivB58(params);
            Log.i("TAG", "BIP32 Extended Private Key:" + priv);
        }
        // 4. 由根私钥生成 第一个HD 钱包
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 5. 定义父路径 H则是加强 imtoken中的eth钱包进过测试发现使用的是此方式生成
        List<ChildNumber> parentPatheth = parsePath("M/44H/60H/0H/0");
        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/60'/0'/0/0）
        DeterministicKey childeth = dh.deriveChild(parentPatheth, true, true, new ChildNumber(0));
        Log.i("TAG", "generateBip44Wallet: 44钥匙对  ETH私钥 = " + childeth.getPrivateKeyAsHex());
        Log.i("TAG", "generateBip44Wallet: 44钥匙对  ETH公钥 = " + childeth.getPublicKeyAsHex());
        byte[] privateKeyByte = childeth.getPrivKeyBytes();
//        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        //通过私钥生成公私钥对
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
        Log.i("TAG", "generateBip44Wallet: 钥匙对  ETH私钥 = " + Numeric.toHexStringNoPrefix(keyPair.getPrivateKey()));
        Log.i("TAG", "generateBip44Wallet: 钥匙对  ETH公钥 = " + Numeric.toHexStringNoPrefix(keyPair.getPublicKey()));
        WalletFile walletFile = Wallet.createLight(password, keyPair);
        Log.i("TAG", "generateBip44Wallet: ETH地址 = " + Keys.toChecksumAddress(walletFile.getAddress()));
        String b = Keys.toChecksumAddress(walletFile.getAddress()).substring(2,Keys.toChecksumAddress(walletFile.getAddress()).length());
        Data.setethaddress(b);Data.sethotethprv(Numeric.toHexStringNoPrefix(keyPair.getPrivateKey()));
        try {
            Bitmap codeBitmap = Utils.createCode(b);
            Data.setethimgCode(codeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            TestBip44BTC();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaibeiWallet(walletFile, mnemonic,keyPair);
    }
    public static List<ChildNumber> btcparsePath(@Nonnull String path) {
        String[] parsedNodes = path.replace("m", "").split("/");
        List<ChildNumber> nodes = new ArrayList<>();
        for (String n : parsedNodes) {
            n = n.replaceAll(" ", "");
            if (n.length() == 0) continue;
            boolean isHard = n.endsWith("'");
            if (isHard) n = n.substring(0, n.length() - 1);
            int nodeNumber = Integer.parseInt(n);
            nodes.add(new ChildNumber(nodeNumber, isHard));
        }
        return nodes;
    }
    @SuppressLint("LongLogTag")
    public static void TestBip44BTC() throws Exception {
//        DeterministicSeed deterministicSeed = null;
//        try {
//            deterministicSeed = new DeterministicSeed(Data.gethotzjc(), null, "", 0);
//        } catch (UnreadableWalletException e) {
//            e.printStackTrace();
//        }
//        DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
//        //这里运用了BIP44里面提到的算法, 44'是固定的, 后面的一个0'代表的是币种BTC
//        NetworkParameters networkParameters  = TestNet3Params.get();
//        String path = "m/44'/1'/0'/0";
//        BigInteger privkeybtc = deterministicKeyChain.getKeyByPath(btcparsePath(path), true).getPrivKey();
//        ECKey ecKey = ECKey.fromPrivate(privkeybtc);
//        String publicKey = ecKey.getPublicKeyAsHex();
//        String privateKey = ecKey.getPrivateKeyEncoded(networkParameters).toString();
//        String address = ecKey.toAddress(networkParameters).toString();
//        LogCook.d("btc私钥：",privateKey);
//        LogCook.d("btc公钥：",publicKey);
//        LogCook.d("btc地址：",address);
//        Data.setbtcaddress(address);Data.sethotbtcprv(privkeybtc.toString());Data.sethotbtcpub(publicKey);

        DeterministicSeed deterministicSeed = new DeterministicSeed(Data.gethotzjc(), null, "", 0);
        Log.i("BIP39 seed:{}", deterministicSeed.toHexString());

        /**生成根私钥 root private key*/
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(deterministicSeed.getSeedBytes());
        /**根私钥进行 priB58编码*/
        NetworkParameters params = TestNet3Params.get();
        String priv = rootPrivateKey.serializePrivB58(params);
        Log.i("BIP32 extended private key:{}", priv);
        /**由根私钥生成HD钱包*/
        DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(rootPrivateKey);
        /**定义父路径*/
        List<ChildNumber> parsePath = HDUtils.parsePath("44H/1H/0H/0");

        DeterministicKey accountKey0 = deterministicHierarchy.get(parsePath, true, true);
        Log.i("Account extended private key:{}", accountKey0.serializePrivB58(params));
        Log.i("Account extended public key:{}", accountKey0.serializePubB58(params));

        /**由父路径,派生出第一个子私钥*/
        DeterministicKey childKey0 = HDKeyDerivation.deriveChildKey(accountKey0, 0);
//        DeterministicKey childKey0 = deterministicHierarchy.deriveChild(parsePath, true, true, new ChildNumber(0));
        Log.i("BIP32 extended 0 private key:{}", childKey0.serializePrivB58(params));
        Log.i("BIP32 extended 0 public key:{}", childKey0.serializePubB58(params));
        Log.i("0 private key:{}", childKey0.getPrivateKeyAsHex());
        Log.i("0 private key:{}", childKey0.getPrivateKeyAsWiF(params));
        Log.i("0 public key:{}", childKey0.getPublicKeyAsHex());
        Log.i("0 address:{}", String.valueOf(childKey0.toAddress(params)));
        Data.setbtcaddress(String.valueOf(childKey0.toAddress(params)));Data.sethotbtcprv(childKey0.getPrivateKeyAsWiF(params));Data.sethotbtcpub(childKey0.getPublicKeyAsHex());
        try {
            Bitmap codeBitmap = Utils.createCode(String.valueOf(childKey0.toAddress(params)));
            Data.setimgCode(codeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
//        /**由父路径,派生出第二个子私钥*/
//        DeterministicKey childKey1 = HDKeyDerivation.deriveChildKey(accountKey0, 1);
//        Log.i("BIP32 extended 1 private key:{}", childKey1.serializePrivB58(params));
//        Log.i("BIP32 extended 1 public key:{}", childKey1.serializePubB58(params));
//        Log.i("1 private key:{}", childKey1.getPrivateKeyAsHex());
//        Log.i("1 public key:{}", childKey1.getPublicKeyAsHex());
//        Log.i("1 address:{}", String.valueOf(childKey1.toAddress(params)));
    }

    public static BaibeiWallet generateBip44Wallet(String mnemonic, String password)
            throws CipherException {
        //2.生成种子
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        // 3. 生成根私钥 root private key 树顶点的master key
        DeterministicKey child = getDeterministicKeyBip44BySeed(seed);
        byte[] privateKeyByte = child.getPrivKeyBytes();
        //        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        //通过私钥生成公私钥对
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
        Data.sethotethprv(Numeric.toHexStringNoPrefix(keyPair.getPrivateKey()));
        WalletFile walletFile = Wallet.createLight(password, keyPair);
        Log.i("TAG", "generateBip44Wallet: ETH地址 = " + Keys.toChecksumAddress(walletFile.getAddress()));
        String b = Keys.toChecksumAddress(walletFile.getAddress()).substring(2,Keys.toChecksumAddress(walletFile.getAddress()).length());
        Data.setethaddress(b);Data.sethotethprv(Numeric.toHexStringNoPrefix(keyPair.getPrivateKey()));
        try {
            Bitmap codeBitmap = Utils.createCode(b);
            Data.setethimgCode(codeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            TestBip44BTC();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaibeiWallet(walletFile, mnemonic,keyPair);
    }
    public static BaibeiWallet generateBip44WalletByPrivateKey(String privateKey, String password)
            throws CipherException, IOException {
        //2.生成种子
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        // 3. 生成根私钥 root private key 树顶点的master key
        BigInteger pk = Numeric.toBigIntNoPrefix(privateKey);
        byte[] privateKeyByte = pk.toByteArray();
        //        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
        //通过私钥生成公私钥对
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
        WalletFile walletFile = Wallet.createLight(password, keyPair);
        Log.i("TAG", "generateBip44Wallet: 地址 = " + Keys.toChecksumAddress(walletFile.getAddress()));
        return new BaibeiWallet(walletFile, null,keyPair);
    }

    public static BaibeiWallet loadByKeyStore(String password, String keystore)
            throws IOException, CipherException {
            WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);

            ECKeyPair keyPair = Wallet.decrypt(password, walletFile);
        if (TextUtils.isEmpty(walletFile.getAddress())) {
            String address = Numeric.prependHexPrefix(Keys.getAddress(keyPair));
            walletFile.setAddress(address);
        }
        return new BaibeiWallet(walletFile,null,keyPair);
    }


}
