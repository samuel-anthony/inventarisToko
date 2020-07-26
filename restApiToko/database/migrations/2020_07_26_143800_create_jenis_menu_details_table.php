<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateJenisMenuDetailsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('jenis_menu_details', function (Blueprint $table) {
            $table->bigIncrements('jenis_menu_detail_id');
            $table->unsignedBigInteger('jenis_menu_id');
            $table->foreign('jenis_menu_id')->references('jenis_menu_id')->on('jenis_menus');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('jenis_menu_details');
    }
}
