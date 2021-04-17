# JUnit例子

---

## 1、运行测试


### 1.说明
该过程测试分为2个版本：`test`为 JUnit4、`test5`为 JUnit5

区分是因为`powermock`和`jmockit`工具不支持 JUnit5，而默认的 SpringBootTest 优先选择 JUnit5

所有的 JUnit4 测试文件后缀全部加4，例如：`ApplicationTest4`

### 2.运行命令

同时运行所有单元测试
> gradle test test5

## ∞、三方项目

- [assertj](https://github.com/assertj/assertj-core)
  
- [jmockit](https://github.com/jmockit)

- [mockito](https://github.com/mockito/mockito)
- [powermock](https://github.com/powermock/powermock)