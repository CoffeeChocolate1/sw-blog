<template>
  <el-card>
    <h1 class="me-author-name">CoffeeChocolate</h1>
    <div class="me-author-description">
      <span><i class="el-icon-location-outline"></i> &nbsp;湖北&十堰</span>
      <br><br>
      <span><i class="me-icon-job"></i>梦想成为一名高级架构师</span>
    </div>
    <div class="me-author-tool">
      <i @click="showTool(qq)" :title="qq.title" class="iconfont icon-QQ"></i>
      <i @click="showTool(weixin)" :title="weixin.title" class="iconfont icon-weixin"></i>
      <i @click="showTool(bilibili)" :title="bilibili.title" class="iconfont icon-bilibili"></i>
      <i @click="showTool(github)" :title="github.title" class="iconfont icon-github"></i>
      <i @click="showTool(gitee)" :title="gitee.title" class="iconfont icon-gitee"></i>

    </div>
  </el-card>

</template>

<script>
  export default {
    name: 'CardMe',
    data() {
      return {
        qq: {title: 'QQ', message: '2017382883'},
        weixin: {title: '微信', message: 'DoubleSweetWater'},
        bilibili: {
          title: '哔哩哔哩',
          message: '<a target="_blank" href="https://space.bilibili.com/474183892">https://space.bilibili.com/474183892</a>'
        },
        github: {
          title: 'github',
          message: '<a target="_blank" href="https://github.com/CoffeeChocolate1">https://github.com/CoffeeChocolate1</a>'
        },
        gitee: {
          title: 'gitee',
          message: '<a target="_blank" href="https://gitee.com/CoffeeChocolate">https://gitee.com/CoffeeChocolate</a>'
        }

      }
    },
    methods: {
      showTool(tool) {
        this.$message({
          duration: 0,
          showClose: true,
          dangerouslyUseHTMLString: true,
          message: '<strong>' + tool.message + '</strong>'
        });
      }
    }
  }
</script>

<style scoped>
  .me-author-name {
    text-align: center;
    font-size: 30px;
    border-bottom: 1px solid #5FB878;
  }

  .me-author-description {
    padding: 8px 0;
  }

  .me-icon-job {
    padding-left: 16px;
  }

  .me-author-tool {
    text-align: center;
    padding-top: 10px;
  }

  .me-author-tool i {
    cursor: pointer;
    padding: 4px 10px;
    font-size: 30px;
  }
</style>
