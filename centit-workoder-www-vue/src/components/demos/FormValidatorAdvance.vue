<template>
<zpa-column height="auto" :id="id">
  <h3>{{title}}</h3>
  <zpa-row height="auto">
    <zpa-column>
      <zpa-form>
        <zpa-form-group>

        </zpa-form-group>
      </zpa-form>
    </zpa-column>
    <zpa-column>
      <codemirror v-model="code"></codemirror>
    </zpa-column>
  </zpa-row>
  <br>
</zpa-column>
</template>

<script>
  import demo from './demo'

  export default {
  name: 'FormValidatorAdvanceDemo',

  mixins: [demo],

  inject: ['main'],

  data() {
    return {
      id: 'FormValidatorAdvanceDemo',

      title: '高级校验',

      obj: {},

      code: ``,
    }
  },
}

</script>
