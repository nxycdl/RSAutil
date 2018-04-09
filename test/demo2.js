/**
 * Created by Administrator on 2018/4/8.
 */
const request = require('request');
const RSAAES = require('../RSAAESUtils');
const appkey = "9999";
//如果想要将如上的数据post到后台
//第一步;通过APPKEY获取公钥；
const publickey = {
    n: 'b340ca919c70636f04d6e18afb70a0d3ac0c9577ecc0d9832e8c1f71471f70363bf3c8823d907d828001f866c4dd67eca6feeca1e52a4cfdd57439aca84f968e1093acee4e1d28259c443cc40b4ac335790e8db3131fbe92cec7e3c3257598440e752f884849a8f408cda566dab38d8486cf0e9f710563a42cbecc9363ac9227',
    e: '0x10001'
}
let data = {
    "method": "战国",
    "varchar": "ascii + юникод スラ ⑨",
    "inupt": [{"name": "1！@#%……*（（））（)))(*&"}, {"name": "2", "sex": "3"}, {"age": 18}],
    "output": {
        "first": "a",
        "second": "b",
        "thrid": [1, 2, 3, 4, 5, 6, [], [], [{"name": "1", "pr": [1, 2, 3]}]],
        "four": 1
    }
}

//随机数位数必须是16位;
const randmoKey = '1234567890123456'
//1.加密;
let inParams = RSAAES.encode(publickey, data, randmoKey);
inParams.appkey = appkey;
console.log(inParams);
let deData= RSAAES.decode(inParams.data,randmoKey);
console.log(deData);
//2.发起调用;
request({uri: 'http://192.168.200.135:8280/cxzfInterface/invoke', method: "POST",json: true,headers: {"content-type": 'application/json'},body: inParams},(err,httpResponse,body)=>{
//console.log(err);
    //console.log(httpResponse);
    console.log('=================')
    //第五步获取到返回结果;
    console.log(body);
    console.log('=================')

    //第3步解密
    let data= body.outdata;
    data = data.replace(/\n/g,"")
    let deData= RSAAES.decode(data,randmoKey);
    console.log(deData);

});




