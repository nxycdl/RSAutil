/**
 * Created by Administrator on 2018/4/8.
 */
const RSAClient = require('./lib/rsa-client');//客户端rsa加密
const base64 = require('./lib/base64');
const CryptoJS = require('crypto-js');
module.exports = {
    //默认使用cbc，Pkcs7 (the default)
    encode: (publickey, data,randmoKey) => {
        if (typeof(data) === 'object') {
            data = JSON.stringify(data);
        } else {
            data = data.toString();
        }

        var _publicKey = new RSAClient();
        _publicKey.setPublic(publickey.n, publickey.e);

        const enKey = base64.hex2b64(_publicKey.encrypt(randmoKey));

        const key = CryptoJS.enc.Utf8.parse(randmoKey);
        const encryptedData = CryptoJS.AES.encrypt(data, key, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        return {data: encryptedData.toString(), key: enKey};
    },
    decode: (data,randmoKey) => {
        const key = CryptoJS.enc.Utf8.parse(randmoKey);
        const bytes = CryptoJS.AES.decrypt(data.toString(), key, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        return bytes.toString(CryptoJS.enc.Utf8);
    }

}