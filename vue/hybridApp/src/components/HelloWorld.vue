<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h2>Essential Links</h2>
    <ul>
      <li>
        <a
          href="https://vuejs.org"
          target="_blank"
        >
          Core Docs
        </a>
      </li>
      <li>
        <a
          href="https://forum.vuejs.org"
          target="_blank"
        >
          Forum
        </a>
      </li>
      <li>
        <a
          href="https://chat.vuejs.org"
          target="_blank"
        >
          Community Chat
        </a>
      </li>
      <li>
        <a
          href="https://twitter.com/vuejs"
          target="_blank"
        >
          Twitter
        </a>
      </li>
      <br>
      <li>
        <a
          href="http://vuejs-templates.github.io/webpack/"
          target="_blank"
        >
          Docs for This Template
        </a>
      </li>
    </ul>
    <h2>Ecosystem</h2>
    <ul>
      <li>
        <a
          href="http://router.vuejs.org/"
          target="_blank"
        >
          vue-router
        </a>
      </li>
      <li>
        <a
          href="http://vuex.vuejs.org/"
          target="_blank"
        >
          vuex
        </a>
      </li>
      <li>
        <a
          href="http://vue-loader.vuejs.org/"
          target="_blank"
        >
          vue-loader
        </a>
      </li>
      <li>
        <a
          href="https://github.com/vuejs/awesome-vue"
          target="_blank"
        >
          awesome-vue
        </a>
      </li>
    </ul>
  </div>
</template>

<script>

  export default {
    name: 'app',

    data() {
      return {
        pageStack: []
      }
    },

    methods: {
      /* Override default pop behavior and delegate it to the router */
      goBack() {
        // Go to the parent route component
//        console.log(this.$router)
//        this.$router.push({ name: this.$route.matched[this.$route.matched.length - 2].name });
        this.$router.back()
        // this.$router.go(-1); // Could work but might be misleading in some situations
      }
    },

    created() {
      /* Define how routes should be mapped to the page stack.
       * This assumes all the routes contain VOnsPage components
       * and are provided in the 'default' view.
       * For nested named routes or routes that for some reason
       * should not be mapped in VOnsNavigator, filter them out here.
       */
      var router =this.$router
      const mapRouteStack = route => {
        router['_breadscrumb'].extends(route.matched.map(m => m.components.default))
        this.pageStack = router._breadscrumb.map(m => m.extends)
      };

      /* Set initial pageStack depending on current
       * route instead of always pushing 'Home' page
       */
      this.pageStack = this.$route.matched.map(m => m.components.default)
      /* On route change, reset the pageStack to the next route */
      this.$router.beforeEach((to, from, next) => {
        mapRouteStack(to)
        console.log(this.pageStack)
      next()
    });
    }
  }
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
