/**
 * Created by dl on 2018/3/28.
 * Use for RSA encode And sign;
 */
const utils = require('../RSAUtils');
const request=require('request')
const appkey = "9999";
const data = {
    "method": "战国",
    "varchar":"ascii + юникод スラ ⑨",
    "inupt": [{"name": "1！@#%……*（（））（)))(*&"}, {"name": "2", "sex": "3"}, {"age": 18}],
    "output": {"first": "a", "second": "b", "thrid": [1, 2, 3, 4, 5, 6,[],[],[{"name":"1","pr":[1,2,3]}]], "four": 1}
}
//如果想要将如上的数据post到后台
//第一步;通过APPKEY获取公钥；
const publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzQMqRnHBjbwTW4Yr7cKDTrAyVd+zA2YMujB9x\n" +
    "Rx9wNjvzyII9kH2CgAH4ZsTdZ+ym/uyh5SpM/dV0OayoT5aOEJOs7k4dKCWcRDzEC0rDNXkOjbMT\n" +
    "H76SzsfjwyV1mEQOdS+ISEmo9AjNpWbas42Ehs8On3EFY6QsvsyTY6ySJwIDAQAB";

// 第二步Json序列化;
const inData = JSON.stringify(data);
//console.log(inData);
//第三步对这字符串加密;
const encodeData = utils.encode(publicKeyString, inData);
//第三步封装入参;
const inParams = {
    "data": encodeData,
    "appkey": appkey
}

//console.log(inParams);
//第四步调用Post请求;
//request.post('http://127.0.0.1:8280/cxzfInterface/invoke', {form:{key:'value'}})

request({uri: 'http://192.168.10.112:8280/cxzfInterface/invoke', method: "POST",json: true,headers: {"content-type": 'application/json'},body: inParams},(err,httpResponse,body)=>{
    //console.log(err);
    //console.log(httpResponse);
    console.log('=================')
    //第五步获取到返回结果;
    console.log(body);
    console.log('=================')

    //第六步验证是否是服务器发送的结果;
    const isValid = utils.verify(publicKeyString, body.outdata, body.sign);

    if (isValid == true) {
        //第7步将结果decode成字符串;
        console.log("服务器发送过来的消息");
        let resultData = new Buffer(body.outdata, 'base64')
        resultData = resultData.toString('utf8');
        console.log(JSON.parse(resultData));

    } else {
        console.log("不是服务器发送过来的消息");
        //抛出签名异常;
    }

});

