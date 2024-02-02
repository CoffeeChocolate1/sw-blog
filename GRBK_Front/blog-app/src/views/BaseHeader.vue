<template>
  <el-header class="me-area">
    <el-row class="me-header">

<!--      <el-col :span="4" class="me-header-left">-->
<!--        <router-link to="/" class="me-title">-->
<!--          <img src="../assets/img/logo.png" />-->
<!--        </router-link>-->
<!--      </el-col>-->

      <el-col v-if="!simple" :span="16">
        <el-menu :router=true menu-trigger="click" active-text-color="#5FB878" :default-active="activeIndex"
                 mode="horizontal">
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/category/all">文章分类</el-menu-item>
          <el-menu-item index="/tag/all">标签</el-menu-item>
          <el-menu-item index="/archives">文章归档</el-menu-item>

          <el-col :span="4" :offset="4">
            <el-menu-item index="/write"><i class="el-icon-edit"></i>写文章</el-menu-item>
          </el-col>

        </el-menu>
      </el-col>

      <template v-else>
        <slot></slot>
      </template>

      <el-col :span="4">
        <el-menu :router=true menu-trigger="click" mode="horizontal" active-text-color="#5FB878">

          <template v-if="!user.login">
            <el-menu-item index="/login">
              <el-button type="text">登录</el-button>
            </el-menu-item>
            <el-menu-item index="/register">
              <el-button type="text">注册</el-button>
            </el-menu-item>
          </template>

          <template v-else>
            <el-submenu index>
              <template slot="title">
<!--                <img class="me-header-picture" :src="user.avatar"/>-->
<!--                <img class="iconfont icon-weixin"/>-->
                <i  title="guanliyuan.title" class="iconfont icon-guanliyuan"></i>
              </template>
              <el-menu-item index @click="logout"><i class="el-icon-back"></i>退出</el-menu-item>
            </el-submenu>
          </template>
        </el-menu>
      </el-col>

    </el-row>
  </el-header>
</template>

<script>
  export default {
    name: 'BaseHeader',
    props: {
      activeIndex: String,
      simple: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {}
    },
    computed: {
      user() {
        let login = this.$store.state.account.length != 0
        let avatar = this.$store.state.avatar
        return {
          login, avatar
        }
      }
    },
    methods: {
      logout() {
        let that = this
        this.$store.dispatch('logout').then(() => {
          this.$router.push({path: '/'})
        }).catch((error) => {
          if (error !== 'error') {
            that.$message({message: error, type: 'error', showClose: true});
          }
        })
      }
    }
  }

  ! function() {
    //封装方法，压缩之后减少文件大小
    function get_attribute(node, attr, default_value) {
      return node.getAttribute(attr) || default_value;
    }
    //封装方法，压缩之后减少文件大小
    function get_by_tagname(name) {
      return document.getElementsByTagName(name);
    }
    //获取配置参数
    function get_config_option() {
      var scripts = get_by_tagname("script"),
        script_len = scripts.length,
        script = scripts[script_len - 1]; //当前加载的script
      return {
        l: script_len, //长度，用于生成id用
        z: get_attribute(script, "zIndex", -1), //z-index
        o: get_attribute(script, "opacity", 0.5), //opacity
        c: get_attribute(script, "color", "0,0,0"), //color
        n: get_attribute(script, "count", 99) //count
      };
    }
    //设置canvas的高宽
    function set_canvas_size() {
      canvas_width = the_canvas.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
        canvas_height = the_canvas.height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
    }

    //绘制过程
    function draw_canvas() {
      context.clearRect(0, 0, canvas_width, canvas_height);
      //随机的线条和当前位置联合数组
      var e, i, d, x_dist, y_dist, dist; //临时节点
      //遍历处理每一个点
      random_lines.forEach(function(r, idx) {
        r.x += r.xa,
          r.y += r.ya, //移动
          r.xa *= r.x > canvas_width || r.x < 0 ? -1 : 1,
          r.ya *= r.y > canvas_height || r.y < 0 ? -1 : 1, //碰到边界，反向反弹
          context.fillRect(r.x - 0.5, r.y - 0.5, 1, 1); //绘制一个宽高为1的点
        //从下一个点开始
        for (i = idx + 1; i < all_array.length; i++) {
          e = all_array[i];
          //不是当前点
          if (null !== e.x && null !== e.y) {
            x_dist = r.x - e.x, //x轴距离 l
              y_dist = r.y - e.y, //y轴距离 n
              dist = x_dist * x_dist + y_dist * y_dist; //总距离, m
            dist < e.max && (e === current_point && dist >= e.max / 2 && (r.x -= 0.03 * x_dist, r.y -= 0.03 * y_dist), //靠近的时候加速
              d = (e.max - dist) / e.max,
              context.beginPath(),
              context.lineWidth = d / 2,
              context.strokeStyle = "rgba(" + config.c + "," + (d + 0.2) + ")",
              context.moveTo(r.x, r.y),
              context.lineTo(e.x, e.y),
              context.stroke());
          }
        }
      }), frame_func(draw_canvas);
    }
    //创建画布，并添加到body中
    var the_canvas = document.createElement("canvas"), //画布
      config = get_config_option(), //配置
      canvas_id = "c_n" + config.l, //canvas id
      context = the_canvas.getContext("2d"), canvas_width, canvas_height,
      frame_func = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function(func) {
        window.setTimeout(func, 1000 / 45);
      }, random = Math.random,
      current_point = {
        x: null, //当前鼠标x
        y: null, //当前鼠标y
        max: 20000
      },
      all_array;
    the_canvas.id = canvas_id;
    the_canvas.style.cssText = "position:fixed;top:0;left:0;z-index:" + config.z + ";opacity:" + config.o;
    get_by_tagname("body")[0].appendChild(the_canvas);
    //初始化画布大小

    set_canvas_size(), window.onresize = set_canvas_size;
    //当时鼠标位置存储，离开的时候，释放当前位置信息
    window.onmousemove = function(e) {
      e = e || window.event, current_point.x = e.clientX, current_point.y = e.clientY;
    }, window.onmouseout = function() {
      current_point.x = null, current_point.y = null;
    };
    //随机生成config.n条线位置信息
    for (var random_lines = [], i = 0; config.n > i; i++) {
      var x = random() * canvas_width, //随机位置
        y = random() * canvas_height,
        xa = 2 * random() - 1, //随机运动方向
        ya = 2 * random() - 1;
      random_lines.push({
        x: x,
        y: y,
        xa: xa,
        ya: ya,
        max: 6000 //沾附距离
      });
    }
    all_array = random_lines.concat([current_point]);
    //0.1秒后绘制
    setTimeout(function() {
      draw_canvas();
    }, 100);
  }();


</script>

<style>

  .el-header {
    position: fixed;
    z-index: 1024;
    min-width: 100%;
    box-shadow: 0 2px 3px hsla(0, 0%, 7%, .1), 0 0 0 1px hsla(0, 0%, 7%, .1);
  }

  .me-title {
    margin-top: 10px;
    font-size: 24px;
  }

  .me-header-left {
    margin-top: 10px;
  }

  .me-title img {
    max-height: 2.4rem;
    max-width: 100%;
  }

  .me-header-picture {
    width: 36px;
    height: 36px;
    border: 1px solid #ddd;
    border-radius: 50%;
    vertical-align: middle;
    background-color: #5fb878;
  }
</style>
