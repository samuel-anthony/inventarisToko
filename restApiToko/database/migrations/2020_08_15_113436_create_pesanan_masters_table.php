<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePesananMastersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('pesanan_masters', function (Blueprint $table) {
            $table->bigIncrements('pesanan_master_id');
            $table->string('ref_no')->unique();
            $table->string('status');
            $table->string('status_code');
            $table->integer('total_harga');
            $table->integer('total_charges');//charges bisa termasuk pajak + service charge
            $table->integer('total_pesanan');
            $table->unsignedBigInteger('user_id');//userid benerannya
            $table->foreign('user_id')->references('id')->on('users');
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
        Schema::dropIfExists('pesanan_masters');
    }
}
