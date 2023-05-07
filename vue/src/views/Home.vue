<template>
  <div class="wrapper" id="home" style="width: 100%;height: 100vh; overflow-x: hidden;" >
    <el-backtop target=".wrapper"></el-backtop>
    <Search/>
  </div>
</template>

<script>


import Search from "@/components/Search";
export default {
  name: 'Home',
  components: {
    Search,
  },

}
</script>
