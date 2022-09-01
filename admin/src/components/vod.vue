<template>
  <big-file v-bind:input-id="inputId"
            v-bind:text="text"
            v-bind:suffixs="suffixs"
            v-bind:use="use"
            v-bind:after-upload="afterUpload"
            v-bind:shard-size="shardSize"
            v-bind:url="'vod'"></big-file>
</template>

<script>
  import BigFile from "./big-file";
  export default {
    comments:{BigFile},
    name: 'vod',
    props: {
      text: {
        default: "上传VOD"
      },
      inputId: {
        default: "vod-upload"
      },
      suffixs: {
        default: []
      },
      use: {
        default:""
      },
      shardSize: {
        default: 1000 * 1024 * 1024
      },
      afterUpload: {
        type: Function,
        default: null
      },
    },
    methods:{
    },
  }
</script>

