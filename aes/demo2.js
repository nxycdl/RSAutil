/**
 * Created by Administrator on 2018/4/8.
 */
require.config({
    packages: [
        {
            name: 'crypto-js',
            location: './crypto-js',
            main: 'index'
        }
    ]
});

require(["crypto-js/aes", "crypto-js/sha256"], function (AES, SHA256) {
    console.log(SHA256("Message"));
});