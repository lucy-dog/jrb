<template>
  <div class="app-container">
    <!-- 输入表单 -->
    <el-form label-width="120px">
      <el-form-item label="借款额度">
        <el-input-number v-model="integralGrade.borrowAmount" :min="0" />
      </el-form-item>
      <el-form-item label="积分区间开始">
        <el-input-number v-model="integralGrade.integralStart" :min="0" />
      </el-form-item>
      <el-form-item label="积分区间结束">
        <el-input-number v-model="integralGrade.integralEnd" :min="0" />
      </el-form-item>
      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="saveOrUpdate()"
        >
          保存
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import integralGradeApi from '@/api/core/integral-grade'

export default {
  data() {
    return {
      integralGrade: {}, // 初始化数据
      saveBtnDisabled: false, // 保存按钮是否禁用，防止表单重复提交
    }
  },

  created() {
    // console.log(this.$route.params.id)
    //如果id存在则回显数据，没有则是更新
    if (this.$route.params.id) {
      this.fetchDataById(this.$route.params.id)
    }
  },

  methods: {
    saveOrUpdate() {
      // 禁用保存按钮
      this.saveBtnDisabled = true

      //id不存在则是新增数据
      if (!this.integralGrade.id) {
        //调用新增方法
        this.saveData()
      } else {
        //调用更新
        this.updateData()
      }
    },

    //新增数据
    saveData() {
      integralGradeApi.save(this.integralGrade).then((response) => {
        this.$message({
          type: 'success',
          message: response.message,
        })
        this.$router.push('/core/integral-grade/list')
      })
    },

    //更新数据
    updateData() {
      integralGradeApi.updateById(this.integralGrade).then((response) => {
        this.$message({
          type: 'success',
          message: response.message,
        })
        this.$router.push('/core/integral-grade/list')
      })
    },

    // 根据id查询记录
    fetchDataById(id) {
      integralGradeApi.getById(id).then((response) => {
        this.integralGrade = response.data.record
      })
    },
  },
}
</script>
