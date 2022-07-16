<template>
  <div>
    <h1>您好！欢迎进入控制大厅</h1>
  </div>
</template>

<script>
export default {
  name: "welcome",
  mounted: function() {
    this.$parent.activeSidebar("welcome-sidebar");
  },
  methods: {
  }
}
</script>