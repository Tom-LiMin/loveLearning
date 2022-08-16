Confirm = {
  show: function (message, callback) {
    Swal.fire({
      title: '请再三思考下?',
      text: message,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '是，请务必删除!',
      cancelButtonText: '取消',
    }).then((result) => {
      if (result.isConfirmed) {
        if (callback) {
          callback();
        }
      }
    })
  }
}