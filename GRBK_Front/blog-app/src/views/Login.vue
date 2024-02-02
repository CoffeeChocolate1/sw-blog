<template>

  <div id="login" v-title data-title="登录">
    <!--<video preload="auto" class="me-video-player" autoplay="autoplay" loop="loop">
          <source src="../../static/vedio/sea.mp4" type="video/mp4">
      </video>-->

    <div class="me-login-box me-login-box-radius">
      <h1>博客系统 登录</h1>

      <el-form ref="userForm" :model="userForm" :rules="rules">
        <el-form-item prop="account">
          <el-input placeholder="用户名" v-model="userForm.account"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input placeholder="密码" type="password" v-model="userForm.password"></el-input>
        </el-form-item>

        <el-form-item size="small" class="me-login-button">
          <el-button type="primary" @click.native.prevent="login('userForm')">登录</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>

<script>
  export default {
    name: 'Login',
    data() {
      return {
        userForm: {
          account: '',
          password: ''
        },
        rules: {
          account: [
            {required: true, message: '请输入用户名', trigger: 'blur'},
            {max: 10, message: '不能大于10个字符', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {max: 10, message: '不能大于10个字符', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      login(formName) {
        let that = this

        this.$refs[formName].validate((valid) => {
          if (valid) {

            that.$store.dispatch('login', that.userForm).then(() => {
                that.$router.go(-1)
            }).catch((error) => {
              if (error !== 'error') {
                that.$message({message: error, type: 'error', showClose: true});
              }
            })
          } else {
            return false;
          }
        });
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

<style scoped>
  #login {
    min-width: 100%;
    min-height: 100%;
  }

  .me-video-player {
    background-color: transparent;
    width: 100%;
    height: 100%;
    object-fit: fill;
    display: block;
    position: absolute;
    left: 0;
    z-index: 0;
    top: 0;
  }

  .me-login-box {
    position: absolute;
    width: 300px;
    height: 260px;
    background-color: white;
    margin-top: 150px;
    margin-left: -180px;
    left: 50%;
    padding: 30px;
  }

  .me-login-box-radius {
    border-radius: 10px;
    box-shadow: 0px 0px 1px 1px rgba(161, 159, 159, 0.1);
  }

  .me-login-box h1 {
    text-align: center;
    font-size: 24px;
    margin-bottom: 20px;
    vertical-align: middle;
  }

  .me-login-design {
    text-align: center;
    font-family: 'Open Sans', sans-serif;
    font-size: 18px;
  }

  .me-login-design-color {
    color: #5FB878 !important;
  }

  .me-login-button {
    text-align: center;
  }

  .me-login-button button {
    width: 100%;
  }




</style>
