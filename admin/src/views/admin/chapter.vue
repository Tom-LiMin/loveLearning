<template>
  <div>
    <h4 class="lighter">
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/course" class="pink"> {{course.name}} </router-link>
    </h4>
    <hr>
    <p>
      <router-link to="/business/course" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-arrow-left"></i>
        返回课程
      </router-link>
      &nbsp;
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>

    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>ID</th>
        <th>名称</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="chapter in chapters">
        <td>{{chapter.id}}</td>
        <td>{{chapter.name}}</td>
        <td>
          <div class="hidden-sm hidden-xs btn-group">
            <button v-on:click="toSection(chapter)" class="btn btn-white btn-xs btn-info btn-round">
              小节
            </button>&nbsp;
            <button v-on:click="edit(chapter)" class="btn btn-white btn-xs btn-info btn-round">
              编辑
            </button>&nbsp;
            <button v-on:click="del(chapter.id)" class="btn btn-white btn-xs btn-warning btn-round">
              删除
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="chapter.name" class="form-control" placeholder="名称">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{course.name}}</p>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>
</template>

<script>
import Pagination from "../../components/pagination";
import course from "@/views/admin/course";
export default {
  components: {Pagination},
  name: "chapter",
  data: function() {
    return {
      chapter: {},
      chapters: [],
      course: {},
    }
  },
  mounted: function() {
    let _this = this;
    _this.$refs.pagination.size = 5;
    let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
    if (Tool.isEmpty(course)) {
      _this.$router.push("/welcome");
    }
    _this.course = course;
    _this.list(1);
    // sidebar激活样式方法一
    this.$parent.activeSidebar("business-chapter-sidebar");

  },
  methods: {
    /**
     * 点击【新增】
     */
    add() {
      let _this = this;
      _this.chapter = {};
      $("#form-modal").modal("show");
    },

    /**
     * 点击【编辑】
     * @param chapter
     */
    edit(chapter) {
      let _this = this;
      // _this.chapter =  chapter;
      _this.chapter =  $.extend({}, chapter);
      $("#form-modal").modal("show");

    },

    /**
     * 列表查询
     */
    list(page) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER+'/business/admin/chapter/list', {
        page: page,
        size: _this.$refs.pagination.size,
        courseId: _this.course.id
      }).then((response)=>{
        Loading.hide();
        let resp = response.data;
        _this.chapters = resp.content.list;
        _this.$refs.pagination.render(page, resp.content.total);
        // toast.success("您要的已摆上桌！");
      })
    },

    /**
     * 点击【保存】
     */
    save() {
      let _this = this;

      // 保存校验
      if (!Validator.require(_this.chapter.name, "名称")
          || !Validator.length(_this.chapter.courseId, "课程ID", 1, 8)) {
        return;
      }

      _this.chapter.courseId = _this.course.id;

      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/chapter/save', _this.chapter).then((response)=>{
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.list(1);
          Toast.success("保存成功！");
        } else {
          Toast.warning(resp.message);
        }
      })
    },

    /**
     * 点击【删除】
     */
    del(id) {
      let _this = this;
      Confirm.show("该操作不可撤销，您确定要删除吗？",function () {
        Loading.show();
        _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/chapter/delete/' + id).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            _this.list(1);
            Toast.success("删除已完成.");
          }
        })

        Swal.fire(
            '已删除!',
            '该大章已被删除.',
            'success',
        )
      })

      /*Swal.fire({
        title: '请再三思考下?',
        text: "该操作不可撤销，真的要删除吗!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '是，请务必删除!'
      }).then((result) => {
        if (result.isConfirmed) {

          _this.$ajax.delete('http://127.0.0.1:9000/business/admin/chapter/delete/' + id).then((response)=>{
            console.log("删除大章列表结果：", response);
            let resp = response.data;
            if (resp.success) {
              _this.list(1);
              Toast.success("删除已完成.");
            }
          })

          Swal.fire(
              '已删除!',
              '该大章已被删除.',
              'success',
          )
        }
      })*/

    },

    /**
     * 点击【小节】
     */
    toSection(chapter) {
      let _this = this;
      SessionStorage.set(SESSION_KEY_CHAPTER, chapter);
      _this.$router.push("/business/section");
    }
  }
}
</script>