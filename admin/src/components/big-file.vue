<template>
  <div>
    <button type="button" v-on:click="selectFile()" class="btn btn-white btn-default btn-round">
      <i class="ace-icon fa fa-upload"></i>
      {{text}}
    </button>
    <input class="hidden" ref="file" type="file" v-on:change="uploadFile()" v-bind:id="inputId+'-input'">
  </div>
</template>

<script>
export default {
  name: 'file',
  props: {
    text: {
      default: "上传文件"
    },
    inputId: {
      default: "file-upload"
    },
    suffixs: {
      default: []
    },
    use: {
      default:""
    },
    afterUpload: {
      type: Function,
      default: null
    },
  },
  data: function () {
    return {
    }
  },
  methods: {
    uploadFile () {
      let _this = this;
      let formData = new window.FormData();
      let file = _this.$refs.file.files[0];

      // 判断文件格式
      let suffixs = _this.suffixs;
      let fileName = file.name;
      let suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
      let validateSuffix = false;
      for (let i = 0; i < suffixs.length; i++) {
        if (suffixs[i].toLowerCase() === suffix) {
          validateSuffix = true;
          break;
        }
      }
      if (!validateSuffix) {
        Toast.warning("文件格式不正确！只支持上传：" + suffixs.join(","));
        $("#" + _this.inputId + "-input").val("");
        return;
      }

      // 文件分片
      let shardSize = 10 * 1024 * 1024;   // 以10MB为一个分片
      let shardIndex = 1;  // 分片索引
      let start = shardIndex * shardSize; // 当前分片起始位置
      let end = Math.min(file.size,start + shardSize);  // 当前分片结束的位置
      let fileShard = file.slice(start,end);

      // key："file"必须和后端controller参数名一致
      formData.append('file', fileShard);
      formData.append('use', _this.use);
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/file/admin/upload', formData).then((response)=>{
        Loading.hide();
        let resp = response.data;
        console.log("上传文件成功：", resp);
        _this.afterUpload(resp);
        $("#" + _this.inputId + "-input").val("");
      });
    },

    selectFile () {
      let _this = this;
      $("#" + _this.inputId + "-input").trigger("click");
    }
  }
}
</script>

